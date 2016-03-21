from FirstFollow import *


class ParsingTable(object):

    def __init__(self):
        self.table = {}
        for nt in range(20, 28):
            self.table[nt] = {1: [], 2: [], 3: [], 4: [], 5: [], 6: [],
                              7: [], 8: [], 9: [], 10: [], 11: [], 12: [], 99: []}


    def fill_table(self, grammar, sentence_symbol):
        set = Set()
        set.prepare_follow_set_hash_table(grammar, sentence_symbol)
        for production in grammar.grammar_productions:
            for terminal in set.first_set(production.rhs, grammar):
                if terminal in grammar.get_terminals():
                    self.table[production.lhs][terminal].append(str(production))
            if 26 in set.first_set(production.rhs, grammar):
                for terminal in set.follow_set(production.lhs):
                    self.table[production.lhs][terminal].append(str(production))

    def __str__(self):
        string = ""
        for x in range(20, 28):
            string += '\n' + str(x) + ": " +str(self.table[x])
        return string

grammar = Grammar(2)
table = ParsingTable()
table.fill_table(grammar, 20)
print table