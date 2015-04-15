class Tree:
	def __init__(self,entry,branches=()):
		self.entry = entry
		for branch in branches:
			assert isinstance(branch,Tree)
		self.branches = list(branches)
		