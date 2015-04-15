import wit
import json
import sys
def count_fucks(response):
	if not response["outcomes"]:
		return 0
	else:
		return len(response["outcomes"][0].get("entities",{}).get("fuck_type",[]))

def main(time_intervals=1):
	intervals = 0
	total_fucks = 0 
	wit.init()
	access_token = '5OOPLQECDO32JWXIAN5TAPE7JZ7J4UHX'
	while intervals < time_intervals:


		response = wit.voice_query_auto(access_token)
		parse_for_fucks = json.loads(response)
		# print response


# ways_to_say_fuck = ['Fuck', 'fuck', 'Fux', 'fux', 'Fucks','fucks', 'Fuckish' 'fuckish', 'Fucker','fucker','Fucking', 'fucking', 'motherfuck', 'Motherfuck', 'Motherfucking','motherfucking', 'fucked', "mother f******", "mother f****",  "mother f***" , 'f***', "f***" , "f****" ,"f*****", "f******","f*******","f********"]

# def fucks(wordshesaid):
# 	said_lst = wordshesaid.split()
# 	more_lst = [ x for x in said_lst if x in ways_to_say_fuck]
# 	return "Fucks given: ", len(more_lst)


		total_fucks = total_fucks + count_fucks(parse_for_fucks)
		intervals += 1
		print(total_fucks)
		print(parse_for_fucks)

	wit.close()
	print total_fucks
	return total_fucks


if __name__ == "__main__":
	main(int(sys.argv[1]))



