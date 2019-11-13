# -*- encoding: utf8 -*-
import itertools
from DiscreteVariable import *

#Bayes_speaker implementation based on Pablos Bayesian Network


class BayesianNode:
    # Constructor
    def __init__(self, name):
        # Nombre del nodo
        self.name_node = name
        # Lista de todas las variables discretas que contiene el nodo
        self.discrete_variables = []
        # Lista de los nodos hijos del nodo actual
        self.children = []
        # Lista de los nodos padres del nodo actual. Lo hago por conveniencia
        self.parents = []
        # Tabla de probabilidad del nodo
        self.probability_table = {}

        # El nodo en sí mismo, representa una variable discreta, misma que deberán contener sus nodos hijos
        self.discrete_variables.append(DiscreteVariable(name))

    # Método para agregar un nodo hijo
    def add(self, child_bayesian_node):
        # Nos aseguramos de que el parámetro sea tratado como un BayesianNode
        assert isinstance(child_bayesian_node, BayesianNode)

        # Agregamos el nodo a los hijos del nodo actual
        self.children.append(child_bayesian_node)

        # Al nodo hijo, agregamos la variable discreta del nodo actual (Que es el padre)
        child_bayesian_node.add_discrete_variable(self.discrete_variables[-1])
        # Al nodo hijo, le agregamos el nodo actual como un nodo padre
        child_bayesian_node.add_parent(self)


    # Método para agregar un nodo padre
    def add_parent(self, bayesian_node):
        self.parents.append(bayesian_node)

    # Método para agregar variables discretas
    def add_discrete_variable(self, discrete_variable):
        # El uso de "insert" es importante para conservar el orden
        self.discrete_variables.insert(0, discrete_variable)

    # Método para establecer la tabla de probabilidad del nodo
    def set_table(self, probability_table_dict):
        self.probability_table = probability_table_dict

    # Método que regresa la tabla de probabilidad
    @property
    def p(self):
        return self.probability_table

    # Método que calcula la probabilidad de que suceda el evento A, dado el evento B en la Red Bayesiana
    def a_given_b(self, a_fixed_vars_list_dict, b_fixed_vars_list_dict):
        # A UNION B
        a_fixed_vars_list_dict.extend(b_fixed_vars_list_dict)

        # Cociente entre las sumatorias de probabilidad.
        return self.p_sum(a_fixed_vars_list_dict) / self.p_sum(b_fixed_vars_list_dict)

    # Método para calcular la sumatoria de probabilidad
    def p_sum(self, fixed_vars_list_dict):
        sum = 0

        # Creamos la tabla de verdad dado el númeto de variables discretas con las que cuenta el noto actual
        table = list(itertools.product([True, False], repeat=len(self.discrete_variables)))

        # Todas las combinaciones dadas las variables discretas
        print (table)

        # Recorremos toda la tabla de verdad para ir eliminando los casos que contradigan al evento dado
        for row in table[:]:
            for fixed_var_dict in fixed_vars_list_dict:
                var_index = self.discrete_variables.index(fixed_var_dict['var'])
                if row[var_index] != fixed_var_dict['value']:
                    table.remove(row)
                    break

        # Nos queda la tabla que nos interesa
        print (table)

        probability = 0
        parent_probability = 0
        # Recorremos cada registro de la tabla de verdad para ir evaluando su probabilidad.
        for row in table:
            # Por medio de cada valor para las variables de la tabla,
            # vamos localizando las coordenadas de la probabilidad en la tabla de probabilidad
            # i.e. p[True][True][False]
            for index, variable in enumerate(self.discrete_variables):
                # Mientras sea un diccionario me interesa ir más profundo en la tabla
                if isinstance(probability, dict):
                    probability = probability[row[index]]
                # Si el valor no es un diccionario es porque es un número (line: probability = 0)
                # Le asignamos el primer valor de la tabla de probabilidad
                else:
                    probability = self.p[row[index]]

            print (probability)

            # Recorremos los nodos padres
            for parent in self.parents:
                assert BayesianNode(parent)
                # Se recorre cada variable discreta que esté en el padre actual (parent)
                for variable in parent.discrete_variables:
                    # Obtiene el índice de la variable discreta del padre en la lista de
                    # variables discretas del nodo presente
                    index = self.discrete_variables.index(variable)
                    # Mientras sea un diccionario me interesa ir más profundo en la tabla
                    if isinstance(parent_probability, dict):
                        parent_probability = parent_probability[row[index]]
                    # Si el valor no es un diccionario es porque es un número (line: probability = 0)
                    # Le asignamos el primer valor de la tabla de probabilidad
                    else:
                        parent_probability = parent.p[row[index]]

                # Acumulamos la probabilidad de los padres
                probability *= parent_probability

            # Se va sumando la probabilidad
            sum += probability

        return sum


    # Méotodo / propiedad para devolver la variable discreta que representa el nodo
    @property
    def var(self):
        return self.discrete_variables[-1]

    def __str__(self):
        discrete_variables_list_str = "\n\tDiscrete Variables: | "
        for discrete_variable in self.discrete_variables:
            discrete_variables_list_str += str(discrete_variable.name) + " | "


        children_list_str = "\n\tChildren Nodes: | "
        for child in self.children:
            children_list_str += child.name_node + " | "

        parents_list_str = "\n\tParent Nodes: | "
        for parent in self.parents:
            parents_list_str += parent.name_node + " | "

        return "Node " + self.name_node + ":" + \
               discrete_variables_list_str + \
               children_list_str + \
               parents_list_str + "\n"
