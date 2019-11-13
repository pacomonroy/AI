#Bayes_speaker implementation based on Pablos Bayesian Network


class DiscreteVariable:
    def __init__(self, name):
        self.name = name

    def __str__(self):
        return "DiscrVar: "+  self.name
