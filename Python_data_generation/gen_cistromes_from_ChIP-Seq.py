#!/usr/bin/python

############################################################################
# 
# the purpose of this script is to:
#
# a) read the ChIP-Seq data for a given cistrome and format it into the
#    XML format we have chosen to describe cistromes
#
#    need to supply:
#    the name of the file in which the text formatted ChIP-Seq data resides
#    the column the TFBS data we want to use is located in
#    the column the gene product data we want to use is located in
#    the name for our output file
# 
#    these can be supplied via the command line
#
############################################################################

# import the python XML parser 

import os
import stat
import sys
import math

# retrieve the run parameters from the command line

chip_in = sys.argv[1]
col1 = int(sys.argv[2])
col2 = int(sys.argv[3])
chip_out = sys.argv[4]

# open ChIP-Seq data for reading

print "Reading ChIP-Seq data from " + chip_in;

col1 -= 1;
col2 -= 1;

# we haven't read anythng in yet...
data = []
numsegs = 0
lastend = 1
lastchromosome = 'chr0'

# open ChIP-Seq data file
f = open(chip_in, 'r')

# read it a line at a time
for line in f:
	
	# split line into separate data items

	fields = line.split('\t')

	# if the line is data (as opposed to column headings) we need to 
	# add the data to the data[] array

	if (fields[0].find('window') == -1):

		locations = fields[0].split(':')
		thischromosome = locations[0]
		thisstart = int(locations[1])
		thisend = int(locations[2])

		# if we are at the start of a new chromosome we need to be looking for '1' as the start
		if (thischromosome != lastchromosome):
			lastend = 1
			lastchromosome = thischromosome
			print thischromosome + " start location found " + str(thisstart)
	
		if (thisstart == lastend):
		
			#if the TF data column contains '\r\n' we want to make this a null string
			if (fields[col2].find('\r\n') != -1):
				fields[col2].replace("\r\n"," ")

			# read the data into the data array
			print fields
			numsegs+=1
			data.append(fields)
			lastend = thisend

f.close()

print str(numsegs) + " Segments read from file"

# we need to calculate X and Y dimensions for the output cistrome
# ideally X and Y will be factors of the number of Segments read in 
# that are as close as possible to the square root of this number

root = math.sqrt(numsegs)
root = int(math.ceil(root))

# open an XML file for the generated cistrome data

xml = open(chip_out,"w")

xml.write("<xml>")
xml.write("\n")
xml.write("\t<!-- each cistrome will essentially be  a data structure containing Xkb Segments -->")
xml.write("\n")
xml.write("\t<!-- each Segment is described by:  -->")
xml.write("\n")
xml.write("\t<!-- how many TFBS the Segment contains -->")
xml.write("\n")
xml.write("\t<!-- the potential products of transcribing this Segment  -->")
xml.write("\n")
xml.write("\t<!-- products will only be named if they are associated with the regulation of transcription -->")
xml.write("\n")
xml.write("\t<cistrome>")
xml.write("\n")
xml.write("\t\t<xsegs>"),
xml.write(str(root)),
xml.write("</xsegs>")
xml.write("\n")
xml.write("\t\t<ysegs>"),
xml.write(str(root)),
xml.write("</ysegs>")
xml.write("\n")

xml.write("\t\t<chip-seq>")
xml.write("\n")

x = 0
y = 0

# loop from 1 to target (= number of segments read in)

for seg in range((root * root)):

	#if ((((seg*1.0)/root) == int((seg*1.0)/root)) and (seg >0)):
	if (x >= root - 1):
		y += 1

	x = seg - (y*root)

# we want to generate an XML format cistrome...

# generate product data

	if (seg >= numsegs):
	
		product = ['none']
		num_tfbs = 0

	else:

		num_tfbs = int(data[seg][col1])

		if (num_tfbs > 0):

			if (data[seg][col2] != '\r\n'):
			
				product = ['nanog','oct4','sox2']

			else:

				product = ['none']

		else:

			product = ['none']
	
	# output rest of segment specification

	xml.write("\t\t\t<Segment>")
	xml.write("\n")
	xml.write("\t\t\t\t<numTF>"),
	xml.write(str(num_tfbs)),
	xml.write("</numTF>"),
	xml.write("\n")
	xml.write("\t\t\t\t<x>"),
	xml.write(str(x)),
	xml.write("</x>")
	xml.write("\n")
	xml.write("\t\t\t\t<y>"),
	xml.write(str(y)),
	xml.write("</y>")
	xml.write("\n")
	xml.write("\t\t\t\t<Products>")
	xml.write("\n")

	for tf in product:

		xml.write("\t\t\t\t\t<Product>"),
		xml.write(tf),
		xml.write("</Product>")
		xml.write("\n")

	xml.write("\t\t\t\t</Products>")
	xml.write("\n")
	xml.write("\t\t\t</Segment>")
	xml.write("\n")

xml.write("\t\t</chip-seq>")
xml.write("\n")
xml.write("\t</cistrome>")
xml.write("\n")
xml.write("</xml>")
xml.write("\n")

xml.close()
