#Email agent implementation - Francisco Monroy
import time
from itertools import chain
import email
import imaplib

#Credentials, port and host to login into email account
imap_ssl_host = 'imap.gmail.com'  # Gmail host
imap_ssl_port = 993
username = '1234@gmail.com' #Enter username
password = 'password' #Enter password
cont = 0;

#Criteria for selecting emails
criteria = {
    'FROM':    '123@gmail.com', #From which adress the agent receives emails (optional)
    'SUBJECT': 'supplier', #The subject must contain the words "supplier"
    'BODY':    'total price', #The message must contain the words "total price"
}
uid_max = 0


#Method for searching a string or list of words in a message
def find_words(uid_max, criteria):
    c = list(map(lambda t: (t[0], '"'+str(t[1])+'"'), criteria.items())) + [('UID', '%d:*' % (uid_max+1))]
    return '(%s)' % ' '.join(chain(*c))


#Method for obtaining the message block
def obtain_message(msg):
    type = msg.get_content_maintype()
    if type == 'multipart':
        for part in msg.get_payload():
            if part.get_content_maintype() == 'text':
                return part.get_payload()
    elif type == 'text':
        return msg.get_payload()

#Login
server = imaplib.IMAP4_SSL(imap_ssl_host, imap_ssl_port)
server.login(username, password)
server.select('INBOX')

#Use the search method with the criteria in the mail server
result, data = server.uid('search', None, find_words(uid_max, criteria))

uids = [int(s) for s in data[0].split()]
if uids:
    uid_max = max(uids)

#Logout of the server
server.logout()


while 1:

    #Agent starts listening for incoming messages to Inbox

    server = imaplib.IMAP4_SSL(imap_ssl_host, imap_ssl_port)
    server.login(username, password)
    server.select('INBOX')

    result, data = server.uid('search', None, find_words(uid_max, criteria))

    uids = [int(s) for s in data[0].split()]
    for uid in uids:

        #Read messages
        if uid > uid_max:
            result, data = server.uid('fetch', uid, '(RFC822)')  # fetch entire message
            msg = email.message_from_string(data[0][1])

            uid_max = uid

            text = obtain_message(msg)
            print 'New message from supplier:'
            print text

            #Find and print the total price of the supplier
            price = [int(i) for i in text.split() if i.isdigit()]
            print ("The price is : " + str(price))
            aux = int(''.join(str(i) for i in price))

            #Sum and print the accumulated price
            cont += aux
            print ("Acumulated price is: " + str(cont))


    server.logout()
    time.sleep(1)

print ("Acumulated price is: " + str(cont))
