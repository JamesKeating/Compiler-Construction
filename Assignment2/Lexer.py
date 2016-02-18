from symbol_table import Trie

class Lexer(object):

    def __init__(self):
        self.state = 1
        self.put_back = False
        self.current_token = ''
        self.symbol_table = Trie()
        default_id = [
            'Private', 'Public', 'Protected', 'Static',
            'Primary', 'Integer', 'Exception', 'Try'
        ]

        for word in default_id:
            self.symbol_table.process_string(word, True)

    def putting_back(self, file):
        if self.put_back:
            self.put_back = False
            return True

        return False

    def driver(self, filename):
        f = open(filename, 'r')

        while True:
            if not self.putting_back(f):
                char = f.read(1)

            if self.dfa(char):
                print self.create_token()
            if not char: break

    def state_1(self, char):
        if char == '':
            return 0

        elif char.isspace():
            self.state = 1

        elif self.is_special_char(char):
            self.state = 2

        elif char.isdigit():
            self.state = 3

        elif char.isalpha():
            self.state = 5

        elif self.is_string_flag(char):
            self.state = 7

    def state_3(self, char):
        if not char.isdigit():
            self.state = 4

    def state_5(self, char):
        if not char.isalpha():
            self.state = 6

    def state_7(self, char):
        if char == '':
            self.state = 9

        elif self.is_tilda(char):
            self.state = 8

        elif self.is_string_flag(char):
            self.state = 9

    def state_8(self, char):
        self.state = 7

    def dfa(self, char):
        dispatcher = {
            1: self.state_1,
            3: self.state_3,
            5: self.state_5,
            7: self.state_7,
            8: self.state_8,
        }
        dispatcher.get(self.state)(char)

        if self.state in {2,3,5,7,9}:
            self.current_token += char

        if self.state in {1,3,5,7,8}:
            return False

        if self.state in {4,6}:
            self.put_back = True

        return True

    def create_token(self):
        if self.state == 2:
            special_char = {'(':'lpar', ')':'rpar', ';':'semicolon'}
            attribute = 'zero'
            token_type = special_char[self.current_token[-1:]]

        elif self.state == 4:
            token_type = 'int'
            attribute = self.current_token

        elif self.state == 6:
            if self.valid_id(self.current_token):
                token_type = 'id'
                attribute = self.current_token

            else:
                token_type = 'error'
                attribute = 'zero'

        elif self.state == 9:
            token_type = 'string'
            attribute = self.current_token[1:-1]

        self.current_token = ''
        self.state = 1
        return Token(token_type, attribute)

    def is_special_char(self,  char):
        return ord(char) in {40, 41, 59}

    def is_string_flag(self, char):
        return ord(char) == 34

    def is_tilda(self, char):
        return ord(char) == 126

    def valid_id(self, id):
        if id[:1].isupper():
            flag = False
        else:
            flag = True

        return self.symbol_table.process_string(id, flag) != -1

class Token(object):
    def __init__(self, token_type, attribute):
        self.token = [token_type, attribute]

    def __str__(self):
        return '< ' + ' , '.join(self.token) + ' >'

test = Lexer()
test.driver('test.txt')
