# Author James Keating 13508803
# Make sure indentation is set to spaces (tabs by default) to run, done to allow 8 space leading tabs
class TrieNode(object):

        def __init__(self):
                self.identifier_number = None
                self.is_identifier = False
                self.children = {}


class Trie(object):

        def __init__(self):
                self.root_node = TrieNode()
                self.current_node = self.root_node
                self.size = 0

        def insert_node(self, value, parent):
                new_node = TrieNode()
                new_node.identifier_number = self.size
                parent.children[value] = new_node
                self.size += 1

        def process_char(self, value, flag=False):      # method 1
                if value not in self.current_node.children:
                        if flag:        # if dynamic add node
                                self.insert_node(value, self.current_node)
                        else:
                                self.current_node = False
                                return

                self.current_node = self.current_node.children[value]

        def get_identifier(self, flag):  # method 2
                if type(self.current_node)is TrieNode and self.current_node.is_identifier or flag:
                        self.current_node.is_identifier = True      #make node an identifier if dynamic
                        id_num = self.current_node.identifier_number
                        self.current_node = self.root_node
                        return id_num

                self.current_node = self.root_node  #reset current node
                return -1

        def process_string(self, str, flag=False):  # method 3
                for char in str:
                        self.process_char(char, flag)
                        if not self.current_node: break

                return self.get_identifier(flag)