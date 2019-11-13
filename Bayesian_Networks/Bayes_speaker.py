# -*- encoding: utf8 -*-
from BayesianNode import *

#Bayes_speaker implementation based on Pablos Bayesian Network


personal_emergency_node = BayesianNode("personal emergency")
transportation_problem_node = BayesianNode("transportation problem")
cancel_conference_node = BayesianNode("cancel conference")

cancel_conference_network = personal_emergency_node

personal_emergency_node.add(transportation_problem_node)
personal_emergency_node.add(cancel_conference_node)
transportation_problem_node.add(cancel_conference_node)

personal_emergency_node.set_table({True: 0.12, False: 0.88})

transportation_problem_node.set_table({True: 0.05, False: 0.95})


cancel_conference_node.set_table({
    True: {
        True: {
            True: 1.0,
            False: 0.0
        },
        False: {
            True: 0.85,
            False: 0.15
        }
    },
    False: {
        True: {
            True: 0.5,
            False: 0.5
        },
        False: {
            True: 0.0,
            False: 1.0
        }
    }
})


print (cancel_conference_node.a_given_b([   {'var': personal_emergency_node.var, 'value': False}     ],
                                         [   {'var': transportation_problem_node.var, 'value': False}    ]))
print("-------------------")
print (cancel_conference_node.a_given_b([   {'var': personal_emergency_node.var, 'value': True}, {'var': transportation_problem_node.var, 'value': False}     ],
                                         [   {'var': cancel_conference_node.var, 'value': True}    ]))
