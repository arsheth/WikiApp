
from __future__ import unicode_literals
import sqlite3 as lite
import sys
import pickle

con = None

try:
	reload(sys)
	sys.setdefaultencoding('utf-8')
	con = lite.connect('wiki_art_db')
    
	cur = con.cursor()
	fo = open("popular_ranks.csv", "r")
	line = fo.readline()
	while line:
	    line = fo.readline()
	    line = line.decode("utf-8", "replace")
	    parts = line.split(',')
            if len(parts) == 2:
        	image_id = parts[0]
        	rank = parts[1].replace("\r\n","")
        	cur.execute('update tsp set popular_rank  = ? where image_id = ?;',(rank,image_id))
	con.commit()
except lite.Error, e:
    
    print "Error %s:" % e.args[0]
    sys.exit(1)
    
finally:
    
    if con:
        con.close()


'''
fo = open("extra_info.csv","r")
line = fo.readline()
while line:
	line = fo.readline()
	#line = line.decode("utf-8", "replace")
	parts = line.split(',')
	if len(parts) == 5:
		#name = parts[0].replace("\r\n","")
		#name = name.replace(".jpg","")

		year = parts[1]
		style = parts[2]
		genre = parts[3]
		link = parts[4].replace("\r\n","")
		cur.execute('update art set date  = ?, style = ?, genre = ? where link = ?;',(year,style,genre,link))
con.commit()
'''
