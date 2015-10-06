#!/usr/bin/python

############################################################################
# 
# the purpose of this script is to:
#
# a) read the desired dimensions of the toy data being generated
#
#    data_dimensions.xml
#
# b) create toy cistrome data based on these dimensions
#
############################################################################

# import the python XML parser 

import xml.dom.minidom
import numpy.random
import os
import stat
import random
import shutil
import time
import sys

# open data_dimensions.xml for reading

doc = xml.dom.minidom.parse("data_dimensions.xml")
 
param_name = doc.getElementsByTagName("name")

param_x = doc.getElementsByTagName("x")
param_y = doc.getElementsByTagName("y")

param_npost = doc.getElementsByTagName("npost")
param_nred = doc.getElementsByTagName("nred")
param_nseg = doc.getElementsByTagName("nseg")
   
param_numruns = doc.getElementsByTagName("numRuns")

param_resultpath = doc.getElementsByTagName("writeResultsTo")

param_maxtfbs = doc.getElementsByTagName("maxTFBS")

print "Parameters for cistrome generation read in from command file"
print "------------------------------------------------------------"

name = param_name[0].firstChild.nodeValue
print "name is " + (param_name[0].firstChild.nodeValue) 

x = int(param_x[0].firstChild.nodeValue)
print "x is " + str(int(param_x[0].firstChild.nodeValue))

y = int(param_y[0].firstChild.nodeValue)
print "y is " + str(int(param_y[0].firstChild.nodeValue)) 

npost = (int(param_npost[0].firstChild.nodeValue))
print "number of posts is " + str(int(param_npost[0].firstChild.nodeValue))
      
nred = (int(param_nred[0].firstChild.nodeValue))
print "number of red posts is " + str(int(param_nred[0].firstChild.nodeValue))
      
nseg = (int(param_nseg[0].firstChild.nodeValue))
print "number of segments is " + str(int(param_nseg[0].firstChild.nodeValue))

numruns = (int(param_numruns[0].firstChild.nodeValue))
print "Number of cistromes generated is " + str(int(param_numruns[0].firstChild.nodeValue))

resultpath = (param_resultpath[0].firstChild.nodeValue)
print "Results will be written to " + (param_resultpath[0].firstChild.nodeValue)

max_num_tfbs = (int(param_maxtfbs[0].firstChild.nodeValue))
print "Maximum number of TFBS per segment is " + str(int(param_maxtfbs[0].firstChild.nodeValue))

#shutil.copyfile("data_dimensions.xml", resultpath + "/" + "data_dimensions.xml")

# we want to do all this for numRuns files

for run in range(numruns):

	permutation = numpy.random.permutation(nseg)

	reds = permutation[0:nred]
	posts = permutation[0:npost]

	# open an XML file for the generated cistrome data

	cistrome = 'cistrome_' + str(x) + '_' + str(y) + '_' + str(run) + '.xml'

	output = resultpath + '/' + cistrome

	xml = open(output,"w")

	xml.write("<xml>")
        xml.write("\n")
        xml.write("\t<!-- each cistrome will essentially be  a data structure containing 100kb Segments -->")
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
        xml.write("\t\t<name>"),
	xml.write(name),
	xml.write("</name>")
        xml.write("\n")
#       xml.write("\t\t<branchingRate>"),
#	xml.write(str(m)),
#	xml.write("</branchingRate>")
#       xml.write("\n")
        xml.write("\t\t<xsegs>"),
	xml.write(str(x)),
	xml.write("</xsegs>")
        xml.write("\n")
        xml.write("\t\t<ysegs>"),
	xml.write(str(y)),
	xml.write("</ysegs>")
        xml.write("\n")

	xml.write("\t\t<chip-seq>")
	xml.write("\n")

	# generate cistrome data

	x = int(x)
	y = int(y)

	nred = float(nred)
	npost = float(npost)

	for p in range(y):

		for q in range(x):

			num_of_segment = (q * 234) + p

			num_tfs = 0
			product = ['none_at_all']

			# if num_of_segment is in array posts then segment is a post

			if num_of_segment in posts:

				num_tfs = random.randint(1,max_num_tfbs)
				product = ['none']

			# if num_of_segment is in array reds then segment is red

			if num_of_segment in reds:

				num_tfs = random.randint(1,max_num_tfbs)
				product = ['nanog','oct4','sox2']

			# output a cistrome data file

			xml.write("\t\t\t<Segment>")
			xml.write("\n")
			xml.write("\t\t\t\t<numTF>"),
			xml.write(str(num_tfs)),
			xml.write("</numTF>"),
			xml.write("\n")
			xml.write("\t\t\t\t<x>"),
			xml.write(str(q)),
			xml.write("</x>")
			xml.write("\n")
			xml.write("\t\t\t\t<y>"),
			xml.write(str(p)),
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
