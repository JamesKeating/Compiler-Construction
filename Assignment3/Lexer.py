# Author James Keating 13508803
# Make sure indentation is set to spaces to run, done to allow 8 space leading tabs
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
                # set up initial words in Trie
                for word in default_id:
                        self.symbol_table.process_string(word, True)

        def is_special_char(self,  char):       # checks for ( ) ;
                return ord(char) in {40, 41, 59}

        def is_string_flag(self, char):         # checks for "
                return ord(char) == 34

        def is_tilda(self, char):               # checks for ~
                return ord(char) == 126

        def putting_back(self):
                if self.put_back:
                        self.put_back = False
                        return True

                return False

        def driver(self, filename):
                file_pointer = open(filename, 'r')
                while True:
                        if not self.putting_back():
                                char = file_pointer.read(1)

                        if self.dfa(char):
                                print self.create_token()

                        if not char: break

        def dfa(self, char):
                dispatcher = {
                    1: self.state_1,
                    3: self.state_3,
                    5: self.state_5,
                    7: self.state_7,
                    8: self.state_8,
                }
                dispatcher.get(self.state)(char)

                if self.state in {2, 3, 5, 7, 9}:       # adds current char to string for building token
                        self.current_token += char

                if self.state in {1, 3, 5, 7, 8}:       # not accepting state
                        return False

                if self.state in {4, 6}:                # put back last char
                        self.put_back = True

                return True                             # in accepting state

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

                else:
                        self.state = 0

        def state_3(self, char):
                if not char.isdigit():
                        self.state = 4

        def state_5(self, char):
                if not char.islower():
                        self.state = 6

        def state_7(self, char):
                if char == '':
                        self.state = 0

                elif self.is_tilda(char):
                        self.state = 8

                elif self.is_string_flag(char):
                        self.state = 9

        def state_8(self, char):
                self.state = 7

        def create_token(self):
                # Create token based on state dfa accepted
                dispatcher = {
                    0: self.error_token,
                    2: self.special_char_token,
                    4: self.int_token,
                    6: self.id_token,
                    9: self.string_token,
                }
                new_token = dispatcher.get(self.state)()
                self.current_token = ''
                self.state = 1
                return new_token

        def error_token(self):
                return Token('error', 'zero')

        def special_char_token(self):
                special_char = {'(': 'lpar', ')': 'rpar', ';': 'semicolon'}
                return Token(special_char[self.current_token], 'zero')

        def int_token(self):
                return Token('int', self.process_int(self.current_token))# validates and builds int

        def id_token(self):
                if self.valid_id(self.current_token) != -1:
                        return Token('id', self.valid_id(self.current_token))

                return self.error_token()

        def string_token(self):
                    # vector does not exsist in python so used list for attribute
                    return Token('string', list(self.current_token[1:-1]))

        def valid_id(self, id):
                if id[:1].isupper():
                        flag = False
                else:
                        flag = True

                return self.symbol_table.process_string(id, flag)

        def process_int(self, number):
                num_digits = len(number)
                processed_int = 0
                if num_digits > 5:      # more that 5 chars in string int is invalid
                        return -1

                elif num_digits == 5:   # compares each digit one at a time until determined if valid
                        for input, maxint in zip(number, '65534'):
                                if int(input) < int(maxint):
                                        break
                                elif int(input) > int(maxint):
                                        return -1

                for digit in number:    # builds int from string if valid
                        num_digits -= 1
                        processed_int += int(digit) * pow(10, num_digits)

                return processed_int


class Token(object):
        def __init__(self, token_type, attribute):
                self.token = [token_type, attribute]

        def __str__(self):
                if self.token[0] == 'string': attribute = ''.join(self.token[1])
                else: attribute = str(self.token[1])
                return '< ' + self.token[0] +' , ' + attribute + ' >'