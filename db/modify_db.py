
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

	fo = open("genre.csv", "r")
	line = fo.readline()
	while line:
		line = fo.readline()
		line = line.decode("utf-8", "replace")
		parts = line.split(',')
		if len(parts) == 2:
			#name = parts[0].replace("\r\n","")
			#name = name.replace(".jpg","")
			date = parts[0]
			date_color = parts[1].replace("\r\n","")
			#print name, artist, image
			#print date, style, genre, image_id
			cur.execute('update tsp set genre_color  = ? where genre = ?;',(date_color,date))
	con.commit()
except lite.Error, e:
    
    print "Error %s:" % e.args[0]
    sys.exit(1)
    
finally:
    
    if con:
        con.close()
