#!/usr/bin/env python3

import sys
if "--noninteractive" in sys.argv:
    import matplotlib as mpl

    mpl.use("svg")

import frccontroller as fct
import math
import matplotlib.pyplot as plt
import numpy as np
import control as ct





class Template(fct.System):
    def __init__(self, dt):
        """Template subsystem.

        Keyword arguments:
        dt -- time between model/controller updates
        """
        # Holds the units of each of the states
        state_labels = [("Angle", "radians"), ("Angular velocity", "rad/s"), ("Angular Acceleration", "rad/s^2")]

        # Units of the system input
        u_labels = [("Voltage", "V")]
        self.set_plot_labels(state_labels, u_labels)

        fct.System.__init__(
            self,
            # min / max inputs to system. In volts, should always be 12 even if system is capped later
            np.array([[-12.0]]),
            np.array([[12.0]]),
            dt,
            # np.zeros((x,y)) is an empty (all zero values) 2d matrix with x rows and y columns
            # state matrix shape
            np.zeros((3, 1)),
            # input matrix shape
            np.zeros((1, 1)),
        )

    def create_model(self, states, inputs):
        # Number of motors
        num_motors = 2
        # Mass of arm in kg
        m = 2.2675
        # Length of arm in m
        l = .8
        # Arm moment of inertia in kg-m^2
        J = 1 / 3 * m * l ** 2
        # Gear ratio
        G = 1.0 / 2.0

        A = []
        B = []
        C = []
        D = []
        # return fct.models.single_joined_arm(fct.models.MOTOR_775PRO, num_motors, J, G)
        return ct.ss(A, B, C, D)
    def design_controller_observer(self):
        q = [9.42]
        r = [12.0]
        self.design_lqr(q, r)
        # self.place_controller_poles([0.87])
        self.design_two_state_feedforward(q, r)

        q_vel = 1.0
        r_vel = 0.01
        self.design_kalman_filter([q_vel], [r_vel])
        # self.place_observer_poles([0.3])


def main():
    dt = 0.01
    flywheel = Template(dt)
    flywheel.export_java_coeffs("Template")

    flywheel.plot_pzmaps()
    if "--noninteractive" in sys.argv:
        names = ["open-loop", "closed-loop", "observer"]
        for i in range(3):
            plt.figure(i + 1)
            plt.savefig(f"template_pzmap_{names[i]}.svg")

    # Set up graphing
    l0 = 0.1
    l1 = l0 + 5.0
    l2 = l1 + 0.1
    t = np.arange(0, l2 + 5.0, dt)

    refs = []

    # Generate references for simulation
    for i in range(len(t)):
        if t[i] < l0:
            r = np.array([[0]])
        elif t[i] < l1:
            r = np.array([[9000 / 60 * 2 * math.pi]])
        else:
            r = np.array([[0]])
        refs.append(r)

    x_rec, ref_rec, u_rec, y_rec = flywheel.generate_time_responses(t, refs)
    flywheel.plot_time_responses(t, x_rec, ref_rec, u_rec)
    if "--noninteractive" in sys.argv:
        plt.savefig("template_response.svg")
    else:
        plt.show()


if __name__ == "__main__":
    main()
