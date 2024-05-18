from Resultat import author

class book:

    def __init__(self, id, title, author_id):
        self.id = id
        self.title = title
        self.author_id = author_id

    def get_id(self):
        return self.id

    def set_id(self, id):
        self.id = id

    def get_title(self):
        return self.title

    def set_title(self, title):
        self.title = title

    def get_author_id(self):
        return self.author_id

    def set_author_id(self, author_id):
        self.author_id = author_id

