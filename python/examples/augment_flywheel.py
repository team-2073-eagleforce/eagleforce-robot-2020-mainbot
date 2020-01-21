#!/usr/bin/env python3

import sys

if "--noninteractive" in sys.argv:
    import matplotlib as mpl

    mpl.use("svg")

import frccontroller as fct
import math
import matplotlib.pyplot as plt
import numpy as np


class Flywheel(fct.System):
    def __init__(self, dt):
        """Flywheel subsystem.

        Keyword arguments:
        dt -- time between model/controller updates
        """
        state_labels = [("Angular velocity", "rad/s")]
        u_labels = [("Voltage", "V")]
        self.set_plot_labels(state_labels, u_labels)

        fct.System.__init__(
            self,
            np.array([[-12.0]]),
            np.array([[12.0]]),
            dt,
            np.zeros((1, 1)),
            np.zeros((1, 1)),
        )

    def create_model(self, states, inputs):
        # Number of motors
        num_motors = 1.0
        # Flywheel moment of inertia in kg-m^2
        J = 0.000293 * 5.121 + .003
        # Gear ratio
        G = 3.333333

        return fct.models.flywheel(fct.models.MOTOR_CIM, num_motors, J, G)

    def design_controller_observer(self):
        q = [9]
        r = [8]
        self.design_lqr(q, r)
        # self.place_controller_poles([0.87])
        self.design_two_state_feedforward(q, r)

        q_vel = 1.0
        r_vel = 0.01
        self.design_kalman_filter([q_vel], [r_vel])
        # self.place_observer_poles([0.3])


def main():
    dt = 0.01
    flywheel = Flywheel(dt)
    # flywheel.export_cpp_coeffs("Flywheel", "subsystems/")
    flywheel.export_java_coeffs("Flywheel")

    flywheel.plot_pzmaps()
    if "--noninteractive" in sys.argv:
        names = ["open-loop", "closed-loop", "observer"]
        for i in range(3):
            plt.figure(i + 1)
            plt.savefig(f"flywheel_pzmap_{names[i]}.svg")

    # Set up graphing
    l0 = 0.5
    l1 = l0 + 5.0
    l2 = l1 + 0.5
    t = np.arange(0, l2 + 5.0, dt)

    refs = []
    max_v = 1300 / 60 * 2 * math.pi
    # Generate references for simulation
    for i in range(len(t)):
        if t[i] < l0:
            r = np.array([[max_v/(l0/dt) * i]])
        elif t[i] < l1:
            r = np.array([[max_v]])
        elif t[i] < l1 + .5:
            r = np.array([[max_v - max_v/(l0/dt) * (i - l1/dt)]])
        else:
            r = np.array([[0]])
        refs.append(r)

    x_rec, ref_rec, u_rec, y_rec = flywheel.generate_time_responses(t, refs)
    flywheel.plot_time_responses(t, x_rec, ref_rec, u_rec)
    if "--noninteractive" in sys.argv:
        plt.savefig("flywheel_response.svg")
    else:
        plt.show()


if __name__ == "__main__":
    main()
