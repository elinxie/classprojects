
from Tkinter import *
import Tkinter
import tkMessageBox

import wit
import json

app= Tk()
app.title("GUI Example")
app.geometry('450x300+200+200')

def count_fucks(response):
	if not response["outcomes"]:
		return 0
	else:
		return len(response["outcomes"][0].get("entities",{}).get("fuck_type",[]))


def main(interval=2):
	i=0
	total_fucks=0
	while i<interval:
		access_token = '5OOPLQECDO32JWXIAN5TAPE7JZ7J4UHX'
		wit.init()
		wit.voice_query_start(access_token)

		response = wit.voice_query_stop()
		parse_for_fucks = json.loads(response)



		print(response)
		total_fucks = total_fucks +count_fucks(parse_for_fucks)

		wit.close()
		i = i + 1
	return total_fucks




def beenClicked():
    tkMessageBox.showinfo("Counting your Fucks", "Counting your fucks for a little over whatever intervals of ten seconds you put in...little bitch...sorry you have to click the ok to start")
    intervalValue = custName.get()
    fucksValues = main(intervalValue)
    tkMessageBox.showinfo("Fucks Given:",fucksValues)
    changeLabel()
    return
def changeLabel():
    name = "Restart motherfucker" 
    labelText.set(name)
    yourName.delete(0,END)
    yourName.insert(0, "intervals of ten")
    return


labelText = StringVar()
labelText.set('Click Button to count your fucks')
label1 = Label(app, textvariable = labelText ,height=4)
label1.pack()



custName = IntVar(None)
yourName = Entry(app, textvariable = custName)
yourName.pack()




button1 = Button(app,text="Click Here", width=20, command=beenClicked)
button1.pack(side='bottom', padx =15, pady =15)
app.mainloop()
