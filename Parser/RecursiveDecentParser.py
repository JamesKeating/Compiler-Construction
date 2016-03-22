from TokenStream import TokenStream
from Grammar import Grammar


class RDP(object):

    def __init__(self, filename, grammar):
        self.attempted_matches = 0
        self.matched_then_discarded = 0
        self.correctly_matched = 0
        self.grammar = grammar
        self.token_stream = TokenStream(filename)

    def parse(self, nt, start_pos=0):
        result = ""
        if self.recursive_parse(nt, start_pos=0)[0]:
            result += "Success input is valid"
        else:
            result += "Fail input is not valid"
        result += self.display_metrics()
        return result

    def recursive_parse(self, nt, start_pos=0):
        sub_count = 0   #counter for number of succesfull matched rhs (used to count discarded matches)
        for production in self.grammar.grammar_productions:
            next_pos = start_pos
            if production.lhs == nt:
                valid = True
                for symbol in production.rhs:
                    valid = False
                    self.attempted_matches += 1
                    if symbol in self.grammar.get_terminals():
                        if self.token_stream.get_token(next_pos) == symbol:
                            next_pos += 1
                            valid = True
                        else:
                            break

                    else:
                        temp = self.recursive_parse(symbol, next_pos)
                        if temp[0]:
                            valid = True
                            next_pos = temp[1]
                            sub_count += temp[2] + 1
                        else:
                            break

                if valid:
                    self.correctly_matched += 1
                    #print production #uncomment to display correcly matched productions
                    return [True, next_pos, sub_count]

        self.matched_then_discarded += sub_count
        self.correctly_matched -= sub_count
        return [False, 0, sub_count]

    def display_metrics(self):
        metrics = "\nNumber of RHS matches attempted: " + str(self.attempted_matches) +\
                  "\nNumber of RHS matched then later discarded: " + str(self.matched_then_discarded)+\
                  "\nNumber of RHS matched in final result: " + str(self.correctly_matched)
        self.attempted_matches = 0
        self.matched_then_discarded = 0
        self.correctly_matched = 0
        return metrics

start_symbol = 20
grammar = Grammar(2)
test = RDP('test_parse.txt', grammar)
print test.parse(start_symbol)



