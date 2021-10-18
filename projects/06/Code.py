class Code:

	def dest(self, dest_instruction):
		machine_dest = "000"

		if "A" in dest_instruction:
			machine_dest =  "1" + machine_dest[1:]
		if "D" in dest_instruction:
			machine_dest =  machine_dest[:1] + "1" + machine_dest[2:]
		if "M" in dest_instruction:
			machine_dest =  machine_dest[:2] + "1"
   
		return machine_dest

	def comp(self, comp_instruction):
		address = "0"

		if "M" in comp_instruction:
			comp_instruction = comp_instruction.replace("M", "A")
			address = "1"

		dict_comp_inst = {
			"0" : "101010",
			"1" : "111111",
			"-1" : "111010",
			"D" : "001100",
			"A" : "110000",
			"!D" : "001101",
			"!A" : "110001",
			"-D" : "001111",
			"-A" : "110011",
			"D+1" : "011111", 
			"A+1" : "110111", 
			"D-1" : "001110", 
			"A-1" : "110010", 
			"D+A" : "000010", 
			"D-A" : "010011", 
			"A-D" : "000111", 
			"D&A" : "000000",
			"D|A" : "010101"
		}

		machine_comp = address + dict_comp_inst.get(comp_instruction, "000000")

		return machine_comp

	def jump(self, jump_instruction):
		dict_jump = {
			"JGT" : "001",
			"JEQ" : "010",
			"JGE" : "011",
			"JLT" : "100",
			"JNE" : "101",
			"JLE" : "110",
			"JMP" : "111"
		}

		machine_jump = dict_jump.get(jump_instruction, "000")
		return machine_jump
		

		