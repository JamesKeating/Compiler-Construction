from Production import Production


class Grammar(object):

    def __init__(self, grammar_num=1):

        if grammar_num == 1:
            self.grammar_productions = [
                Production(20, [21, 99]),
                Production(21, [23, 22]),
                Production(22, [5, 21]),
                Production(22, [6, 21]),
                Production(22, [26]),
                Production(23, [24]),
                Production(23, [7, 23]),
                Production(24, [3, 21, 4]),
                Production(24, [25]),
                Production(24, [2, 10, 1]),
                Production(24, [2, 12, 1]),
                Production(24, [2, 11, 1]),
                Production(24, [2]),
                Production(25, [8]),
                Production(25, [9]),
                Production(26, [])
            ]
        else:
            self.grammar_productions = [
                Production(20, [21, 99]),
                Production(21, [23, 22]),
                Production(22, [5, 21]),
                Production(22, [6, 21]),
                Production(22, [26]),
                Production(23, [24]),
                Production(23, [7, 23]),
                Production(24, [3, 21, 4]),
                Production(24, [25]),
                Production(24, [2, 27]),
                Production(25, [8]),
                Production(25, [9]),
                Production(26, []),
                Production(27, [10, 1]),
                Production(27, [12, 1]),
                Production(27, [11, 1]),
                Production(27, [26])
            ]

    def get_terminals(self):
        return [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 99]

    def get_nt(self):
        nt_list = []
        for production in self.grammar_productions:
            if production.lhs not in nt_list: nt_list.append(production.lhs)
        return nt_list

    def get_epsilon(self):
        return 26
