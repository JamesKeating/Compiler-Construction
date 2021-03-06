from Grammar import Grammar


class Set(object):

    def __init__(self):
        self.follow_set_hashtable = {}

    def first_set(self, sym_sequence, grammar):
        if len(sym_sequence) == 0:
            return [grammar.get_epsilon()]

        elif sym_sequence[0] in grammar.get_terminals():
            return [sym_sequence[0]]

        else:
            nt = sym_sequence[0]

        f2 = []
        for production in grammar.grammar_productions:
            if production.lhs == nt:
                f2.append(self.first_set(production.rhs, grammar))

        if grammar.get_epsilon() not in f2:
            return [item for sublist in f2 for item in sublist]

        else:
            return f2.merge(self.first_set(sym_sequence.pop(0), grammar))#extend not flatten

    def follow_set(self, nt):
        return self.follow_set_hashtable.get(nt)

    def prepare_follow_set_hash_table(self, grammar, sentence_symbol):
        self.follow_set_hashtable = {20: [], 21: [], 22: [], 23: [], 24: [], 25: [], 26: [], 27: []}
        inheritors_hashtable = {20: [], 21: [], 22: [], 23: [], 24: [], 25: [], 26: [], 27: []}
        self.follow_set_hashtable[sentence_symbol].append(99)

        for production in grammar.grammar_productions:
            r = production.rhs
            length = len(r)
            for index in range(0, length):
                if r[index] in grammar.get_nt():
                    for first_set_sym in self.first_set(r[index+1:], grammar):
                        if first_set_sym == grammar.get_epsilon():
                            inheritors_hashtable[production.lhs].append(r[index])
                        else:
                            self.follow_set_hashtable[r[index]].append(first_set_sym)

        idle = False
        while not idle:
            idle = True
            for key in inheritors_hashtable:
                for nt in inheritors_hashtable[key]:
                    for terminal in self.follow_set_hashtable[key]:
                        if terminal not in self.follow_set_hashtable[nt]: #and nt != grammar.get_epsilon():
                            self.follow_set_hashtable[nt].append(terminal)
                            idle = False


# set = Set()
# grammar = Grammar(2)
# set.prepare_follow_set_hash_table(grammar,20)
# for input in range(20, 28):
#     print set.first_set([input], grammar)
#     print 'follow of : ', input, " = ", set.follow_set(input), "\n"
