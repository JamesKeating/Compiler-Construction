from FirstFollow import *


class ParsingTable(object):

    def __init__(self, grammar, sentence_symbol):
        self.table = {}
        self.fill_table(grammar, sentence_symbol)

    def fill_table(self, grammar, sentence_symbol):
        self.clear_table()
        set = Set()
        set.prepare_follow_set_hash_table(grammar, sentence_symbol)
        for production in grammar.grammar_productions:
            for terminal in set.first_set(production.rhs, grammar):
                if terminal in grammar.get_terminals():
                    self.table[production.lhs][terminal] = production
            if 26 in set.first_set(production.rhs, grammar):
                for terminal in set.follow_set(production.lhs):
                    self.table[production.lhs][terminal] = production

    def clear_table(self):
        for nt in range(20, 28):
            self.table[nt] = {1: [], 2: [], 3: [], 4: [], 5: [], 6: [],
                              7: [], 8: [], 9: [], 10: [], 11: [], 12: [], 99: []}

    def get_table(self):
        return self.table

    def __str__(self):
        string = ""
        for x in range(20, 28):
            string += '\nnt(' + str(x) + "): "
            for y in grammar.get_terminals():
                    string += "\t("+ str(y) + ")" + str(self.table[x][y])
        return string

# grammar = Grammar(2)
# table = ParsingTable(grammar, 20)
# print table