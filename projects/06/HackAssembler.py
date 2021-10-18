#!/usr/bin/env python

import os
import sys
from Parser import Parser
from Code import Code 
from SymbolTable import SymbolTable

class HackAssembler: 

	def __init__(self, path_to_assembly):
		self.parser_assembly = Parser(path_to_assembly)
		self.binary_file = open(path_to_assembly[:-4] + ".hack", "w+")

		self.code_writer = Code()
		self.symbol_table = SymbolTable()

	def loop_to_add_symbols(self):
		count_line = 0
  
		while True: 
			self.parser_assembly.advance()

			if not self.parser_assembly.hasMoreLines():
				break

			if self.parser_assembly.instruction_type() == "L_INSTRUCTION":
				self.__add_inexistent_symbol(count_line)
			else:
				count_line += 1

		self.parser_assembly.end_first_loop(True)

	def loop_to_generate_code(self):
		count_line = 16

		while True:
			self.parser_assembly.advance()
			if not self.parser_assembly.hasMoreLines():
				break

			line = ""
			
			if self.parser_assembly.instruction_type() == "A_INSTRUCTION":
				if self.parser_assembly.symbol().isdigit():
					line = self.__convert_line_in_integer(self.parser_assembly.symbol())
				else:
					if self.symbol_table.contains(self.parser_assembly.symbol()):
						line = self.__convert_line_in_integer(self.parser_assembly.symbol())
					else: 
						self.symbol_table.addEntry(self.parser_assembly.symbol(), count_line)
						line = self.__convert_line_in_integer(self.parser_assembly.symbol())
						count_line += 1
			elif self.parser_assembly.instruction_type() == "C_INSTRUCTION": 
				line = self.__create_c_instruction()
			else:
				continue 
			
			self.binary_file.writelines(line + "\n")

		self.binary_file.close()

	def __create_c_instruction(self):
		c_instruction = ("111" + self.code_writer.comp(self.parser_assembly.comp()) 
						+ self.code_writer.dest(self.parser_assembly.dest())
						+ self.code_writer.jump(self.parser_assembly.jump()))

		return c_instruction

	def  __convert_line_in_integer(self, symbol):
		address = self.symbol_table.getAddress(symbol)

		binary_line = "{0:b}".format(address).zfill(16)

		return binary_line

	def __add_inexistent_symbol(self, line):
		if not self.symbol_table.contains(self.parser_assembly.symbol()):
			self.symbol_table.addEntry(self.parser_assembly.symbol(), line)

def main(argv):
	current_folder = os.getcwd()

	destination = os.path.join(current_folder, argv[1])
	
	assembler = HackAssembler(destination)

	assembler.loop_to_add_symbols()
	assembler.loop_to_generate_code()

if __name__ == '__main__':
	main(sys.argv)