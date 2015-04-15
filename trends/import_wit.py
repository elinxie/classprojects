import wit
access_token = 'Y4TFBMUSWGSW7LRK5ETIAF75JETF6ZK6'
wit.init()
response = wit.voice_query_auto(access_token)
print('Response:{}'.format(response))
wit.close



said_lst = wordshesaid.split()
more_lst = [ x for x in said_lst if x == "Fuck"]
print ("Fucks given", len(more_lst))
