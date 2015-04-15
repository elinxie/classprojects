import wit
import json


access_token = '5OOPLQECDO32JWXIAN5TAPE7JZ7J4UHX'
wit.init()
response = wit.voice_query_auto(access_token)
parse_for_fucks = json.loads(response)


ways_to_say_fuck = ['Fuck', 'fuck', 'Fux', 'fux', 'Fucks','fucks', 'Fuckish' 'fuckish', 'Fucker','fucker','Fucking', 'fucking', 'motherfuck', 'Motherfuck', 'Motherfucking','motherfucking', 'fucked', "mother f******", "mother f****",  "mother f***" , 'f***', "f***" , "f****" ,"f*****", "f******","f*******","f********"]

def fucks(wordshesaid):
	said_lst = wordshesaid.split()
	more_lst = [ x for x in said_lst if x in ways_to_say_fuck]
	return "Fucks given: ", len(more_lst)


print(response)
print ("Fucks give:" , len(parse_for_fucks["outcomes"][0]["entities"]["fuck_type"]))

wit.close()

