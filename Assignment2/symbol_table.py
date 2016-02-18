class TrieNode(object):

    def __init__(self):
        self.identifier_number = None
        self.is_identifier = False
        self.children = {}


class Trie(object):

    def __init__(self):
        self.root_node = TrieNode()
        self.size = 0

    def insert_node(self, value, parent):
        new_node = TrieNode()
        new_node.identifier_number = self.size
        parent.children[value] = new_node
        self.size += 1

    def process_char(self, value, flag=False, node=None):   # method 1
        if node is None: node = self.root_node

        if value not in node.children:
            if flag:
                self.insert_node(value, node)
            else:
                return False

        return node.children[value]

    def get_identifier(self, flag, node):  # method 2
        if type(node)is TrieNode and node.is_identifier or flag:
            node.is_identifier = True
            return node.identifier_number

        return -1

    def process_string(self, str, flag=False, node=None):  # method 3
        for char in str:
            node = self.process_char(char, flag, node)
            if not node: break

        return self.get_identifier(flag, node)

