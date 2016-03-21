class Production(object):

    def __init__(self, lhs, rhs):
        self.lhs = lhs
        self.rhs = rhs

    def nt_in_production(self, all_nt):
        result = []
        for symbol in self.rhs:
            if symbol in all_nt: result.append(symbol)
        return result

    def __str__(self):
        return str(self.lhs) + "-->" + str(self.rhs)
