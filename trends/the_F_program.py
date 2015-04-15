
from Tkinter import *
import Tkinter
import tkMessageBox
import wit
import json

app = Tkinter.Tk()
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
		response = wit.voice_query_auto(access_token)
		parse_for_fucks = json.loads(response)


		print(response)
	    
		total_fucks = total_fucks +count(parse_for_fucks)

		wit.close()
		i = i + 1
	return total_fucks




def beenClicked():
    intervalValue = custName.get()
    tkMessageBox.showinfo("Counting your Fucks",intervalValue)

    
    fucksValues = main(intervalValue)
    tkMessageBox.showinfo("Fucks Given:",fucksValue)
    return
def changeLabel():
    name = "Thanks for the click" 
    labelText.set(name)
    yourName.delete(0,END)
    yourName.insert(0, None )
    return


labelText = StringVar()
labelText.set('Click Button')
label1 = Label(app, textvariable = labelText ,height=4)
label1.pack()

checkBoxVal = IntVar()
checkBox1 = Checkbutton(app, variable = checkBoxVal, text = "Happy?")
checkBox1.pack()

custName = IntVar(None)
yourName = Entry(app, textvariable = custName)
yourName.pack()

relStatus = StringVar()
relStatus.set(None)
radio1 = Radiobutton(app, text = "Single", value = "Single", variable = relStatus, command=beenClicked).pack()
radio1 = Radiobutton(app, text = "Married", value = "Married", variable = relStatus, command=beenClicked).pack()


button1 = Button(app,text="Click Here", width=20, command=beenClicked)
button1.pack(side='bottom', padx =15, pady =15)
app.mainloop()
