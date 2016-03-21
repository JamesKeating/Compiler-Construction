from TokenStream import TokenStream
from Grammar import Grammar


class RDP(object):

    def __init__(self, filename, grammar):
        self.attempted_matches = 0
        self.matched_then_discarded = 0
        self.correctly_matched = 0
        self.grammar = grammar
        self.token_stream = TokenStream(filename)

    def parse(self, nt, start_pos):
        sub_count = 0
        for production in self.grammar.grammar_productions: #choose grammar
            next_pos = start_pos
            if production.lhs == nt:
                for symbol in production.rhs:
                    valid = False
                    self.attempted_matches += 1
                    if symbol in self.grammar.get_terminals():
                        if self.token_stream.get_token(next_pos) == symbol:
                            next_pos += 1
                            valid = True

                        else:
                            break

                    elif symbol == self.grammar.get_epsilon(): valid = True

                    else:
                        temp = self.parse(symbol, next_pos)
                        if temp[0]:
                            valid = True
                            next_pos = temp[1]
                            sub_count += temp[2] + 1

                        else:
                            break

                if valid:
                    self.correctly_matched += 1
                    return [True, next_pos, sub_count]

        self.matched_then_discarded += sub_count
        self.correctly_matched -= sub_count
        return [False, 0, sub_count]

# grammar = Grammar()
# test = RDP('test_parse.txt', grammar)
# print test.parse(20, 0)
# print test.attempted_matches
# print test.matched_then_discarded
# print test.correctly_matched


