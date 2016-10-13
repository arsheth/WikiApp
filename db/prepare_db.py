
from __future__ import unicode_literals
import sqlite3 as lite
import sys


# CREATE TABLE people (position INTEGER PRIMARY KEY,name TEXT,link TEXT,rank INTEGER, cluster INTEGER);
 
con = None

try:
	reload(sys)
	sys.setdefaultencoding('utf-8')
	con = lite.connect('wiki_art_db')
    
	cur = con.cursor()

	fo = open("wikiart.csv", "r")
	line = fo.readline()
	i=1;
	while line:
		line = fo.readline()
    	
		parts =  line.split(',')
		if len(parts) == 5:
			'''
			print parts
			parts[2] = parts[2].replace("\n","").replace("\r","")
			cluster = int(parts[2])
			rank = int(parts[1])
			link = parts[0]
			name = parts[0].replace('_',' ').replace("\"","")
			#cur.execute('insert into people(position,rank,name,link,cluster) values(?,?,?,?,?)',[i, rank, name, link, cluster])
			i+=1
			'''
			name = parts[0].replace("\r\n","")
			link = parts[2].replace("\r\n","")
			cur.execute('insert into art(idx,name,link) values(?,?,?)',[i,name,link])
			i+=1
	con.commit()
except lite.Error, e:
    
    print "Error %s:" % e.args[0]
    sys.exit(1)
    
finally:
    
    if con:
        con.close()
