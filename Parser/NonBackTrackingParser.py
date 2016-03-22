from TokenStream import TokenStream
from ParsingTable import ParsingTable
from Grammar import Grammar


class NBTP(object):

    def __init__(self, filename, grammar):
        self.token_stream = TokenStream(filename)
        self.grammar = grammar
        temp = ParsingTable(grammar, sentence_symbol)
        self.parse_table = temp.get_table()

    def parse(self):
        start_symbol = ll1_grammar.grammar_productions[0].lhs
        output = ""
        position = 0
        sentinel = 99
        match_count = 0
        stack = list()

        stack.append(sentinel)
        stack.append(start_symbol)

        a = self.token_stream.get_token(position)
        t = stack[-1]

        while t != sentinel:
            if t == a:
                stack.pop()
                position +=1
                a = self.token_stream.get_token(position)
            elif t in self.grammar.get_terminals(): return "Input invalid Error occured"
            elif self.parse_table[t][a] == []: return "Input invalid Error occured"
            else:
                match_count += 1
                output += "\n" + str(self.parse_table[t][a])
                stack.pop()
                for symbol in reversed((self.parse_table[t][a]).rhs):
                    stack.append(symbol)
            t = stack[-1]

        return "Input is valid.\nThere were " + str(match_count) +\
               " matched productions.(which are shown below)\n\n" + output

sentence_symbol = 20
ll1_grammar = Grammar(2)
test = NBTP('test_parse.txt', ll1_grammar)
print test.parse()