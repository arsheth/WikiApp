
from __future__ import unicode_literals
import sqlite3 as lite
import sys


# CREATE TABLE people (position INTEGER PRIMARY KEY,name TEXT,link TEXT,rank INTEGER, cluster INTEGER);
#ALTER TABLE art ADD COLUMN rank INTEGER; 

con = None

try:
	reload(sys)
	sys.setdefaultencoding('utf-8')
	con = lite.connect('wiki_art_db')
    
	cur = con.cursor()

	fo = open("ranks.csv", "r")
	line = fo.readline()
	while line:
		line = fo.readline()
		line = line.replace("\r\n","")
		parts = line.split(',')
		if len(parts) == 2:
			#name = parts[0].replace("\r\n","")
			#name = name.replace(".jpg","")
			idx = int(parts[0])
			rank = int(parts[1])
			#print rank,name
			cur.execute('update art set rank = ? where idx = ?',(rank,idx))
	con.commit()
except lite.Error, e:
    
    print "Error %s:" % e.args[0]
    sys.exit(1)
    
finally:
    
    if con:
        con.close()
