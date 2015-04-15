import wit
import json

def main(interval=2):
	i=0
	total_fucks=0
	while i<interval:
		access_token = '5OOPLQECDO32JWXIAN5TAPE7JZ7J4UHX'
		wit.init()
		response = wit.voice_query_auto(access_token)
		parse_for_fucks = json.loads(response)


		print(response)
		total_fucks = total_fucks +len(parse_for_fucks["outcomes"][0]["entities"]["fuck_type"])

		wit.close()
		i = i + 1
	print("Fucks Given: ",total_fucks)

