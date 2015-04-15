import wit
from string import ascii_letters

access_token = '5OOPLQECDO32JWXIAN5TAPE7JZ7J4UHX'
wit.init()
response = wit.voice_query_auto(access_token)


ways_to_say_fuck = ['Fuck', 'fuck', 'Fux', 'fux', 'Fucks','fucks', 'Fuckish' 'fuckish', 'Fucker','fucker','Fucking', 'fucking', 'motherfuck', 'Motherfuck', 'Motherfucking','motherfucking', 'fucked', "mother f******", "mother f****",  "mother f***" , "f***" , "f****" ,"f*****", "f******","f*******","f********"]


def extract_words(text):
    """Return the words in a tweet, not including punctuation.

    >>> extract_words('anything else.....not my job')
    ['anything', 'else', 'not', 'my', 'job']
    >>> extract_words('i love my job. #winning')
    ['i', 'love', 'my', 'job', 'winning']
    >>> extract_words('make justin # 1 by tweeting #vma #justinbieber :)')
    ['make', 'justin', 'by', 'tweeting', 'vma', 'justinbieber']
    >>> extract_words("paperclips! they're so awesome, cool, & useful!")
    ['paperclips', 'they', 're', 'so', 'awesome', 'cool', 'useful']
    >>> extract_words('@(cat$.on^#$my&@keyboard***@#*')
    ['cat', 'on', 'my', 'keyboard']
    """
    '''
    lst ="'"
    previous = 'a'
    for x in text:
        if x in ascii_letters and previous not in ascii_letters:
            lst = lst + "','" + x
        elif x in ascii_letters:
            lst = lst + x
        else:
            previous = x
    '''
    lst = ""
    pile = []
    previous = 'a'
    for x in text:
        if x not in ascii_letters and previous not in ascii_letters:

            lst=lst
        elif x not in ascii_letters:
            if lst != "":
                pile.append(lst)
            
            previous = x
            lst = ""


        else: 
            lst=lst + x
            previous = 'a'
    if lst != "":
        pile.append(lst)


    return pile # You may change/remove this line



def fucks(wordshesaid):
	said_lst = extract_words(wordshesaid)
	more_lst = [ x for x in said_lst if x in ways_to_say_fuck]
	return "Fucks given: ", len(more_lst)/2, more_lst


print(response)
print(fucks(response))
wit.close()

