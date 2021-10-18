import pdb

class Parser:
	
	INS_A = 'A_INSTRUCTION'
	INS_L = 'L_INSTRUCTION'
	INS_C = 'C_INSTRUCTION'

	def __init__(self, path_to_source):
		self.source_code = open(path_to_source, "r")
		self.EOF = False
		self.line = ""

	def hasMoreLines(self):
		if (self.EOF) == False:
			return True
		else: 
			return False

	def advance(self):
		current_line = self.source_code.readline()
  
		if not current_line:
			self.EOF = True
			return 

		current_line_without_comments = self.__erase_comments(current_line)
		current_line_formatted = self.__trim_white_space(current_line_without_comments)

		if current_line_formatted == "":
			self.advance()
		else:
			self.line = current_line_formatted
		
	def instruction_type(self):
		first_char = self.line[0]

		dict_type = {
			"@" : self.INS_A,
			"(" : self.INS_L,
			"" : "empty_line"
		}

		return dict_type.get(first_char, self.INS_C)
	
	def symbol(self):
		if self.instruction_type() == self.INS_A:
			symbol = self.line[1:]
		elif self.instruction_type() == self.INS_L:
			symbol = self.line[self.line.find('(')+1:self.line.find(')')]

		return symbol

	def dest(self):
		if "=" in self.line:
			return self.line.split('=')[0]

		return ""

	def comp(self):
		comp_str = self.line
  
		if ";" in self.line:
			comp_str =	self.line.split(";")[0]
		if "=" in self.line:
			comp_str = self.line.split('=')[1]

		return comp_str

	def jump(self): 
		jump_str = ""
		if ";" in self.line:
			jump_str = self.line.split(';')[1]

		return jump_str 

	def end_first_loop(self, flag):
		if flag:
			self.source_code.seek(0, 0)
			self.EOF = False 

	def __erase_comments(self, current_line):
		comment_string = '//'
		line_without_comments = current_line.split(comment_string, 1)[0]

		return line_without_comments

	def __trim_white_space(self, current_line):
		trimmed_str = ''.join(current_line.split())

		return trimmed_str