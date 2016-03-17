class TokenStream(object):

    def __init__(self, file_name):
        self.file_name = file_name

    def package_steam(self):
        file_pointer = open(self.file_name, 'r')
        token = []
        package = []
        for number in file_pointer.read().split():
            token.append(number)
            if token.__len__() > 1:
                package.append(token)
                token = []

        return package


class Production(object):

    def __init__(self, lhs, rhs):
        self.production = [lhs, rhs]


test = TokenStream('test_parse.txt')
package = test.package_steam()
print package