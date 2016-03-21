class TokenStream(object):

    def __init__(self, file_name):
        self.file_name = file_name
        self.stream = self.build_stream()

    def build_stream(self):
        file_pointer = open(self.file_name, 'r')
        token = []
        stream = []
        for number in file_pointer.read().split():
            token.append(int((number)))
            if token.__len__() > 1:
                stream.append(token)
                token = []

        return stream

    def get_token(self, row):
        return self.stream[row][0]

    def get_token_stream(self):
        return self.stream