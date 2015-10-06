#!/usr/bin/python

############################################################################
# 
# the purpose of this script is to:
#
# a) read the desired dimensions of the toy data being generated
#
#    data_dimensions_overlap.xml
#
# b) create overlapping toy cistromes based on these dimensions
#
# we assume initially that we are dealing with two equally sized cistromes..
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

# open data_dimensions_overlap.xml for reading

doc = xml.dom.minidom.parse("data_dimensions_overlap.xml")
 
param_name = doc.getElementsByTagName("name")

param_x = doc.getElementsByTagName("x")
param_y = doc.getElementsByTagName("y")
param_nseg = doc.getElementsByTagName("nseg")

param_nwhite_unshared = doc.getElementsByTagName("nwhite_unshared")
param_nred_unshared = doc.getElementsByTagName("nred_unshared")
   
param_nwhite_shared = doc.getElementsByTagName("nwhite_shared");
param_nred_shared = doc.getElementsByTagName("nred_shared");

param_resultpath = doc.getElementsByTagName("writeResultsTo")

param_maxtfbs = doc.getElementsByTagName("maxTFBS")

# initialise some useful arrays

# one each of these arrays per cistrome
name = []
nwhite_unshared = []
nred_unshared = []
unshared_reds = []
unshared_whites = []

# one each of these arrays per set of cistromes
shared_reds = []
shared_whites = []

print "Parameters for cistrome generation read in from command file"
print "------------------------------------------------------------"

for i in range(len(param_name)):
	name.append(param_name[i].firstChild.nodeValue)
	print "name is " + str(param_name[i].firstChild.nodeValue) 

print "\nCistrome dimensions:"

x = int(param_x[0].firstChild.nodeValue)
print "x is " + str(int(param_x[0].firstChild.nodeValue))

y = int(param_y[0].firstChild.nodeValue)
print "y is " + str(int(param_y[0].firstChild.nodeValue)) 

nseg = (int(param_nseg[0].firstChild.nodeValue))
print "number of segments is " + str(int(param_nseg[0].firstChild.nodeValue))

nred_shared = (int(param_nred_shared[0].firstChild.nodeValue))
nwhite_shared = (int(param_nwhite_shared[0].firstChild.nodeValue))

for i in range(len(param_name)):
	nwhite_unshared.append(int(param_nwhite_unshared[i].firstChild.nodeValue))
	nred_unshared.append(int(param_nred_unshared[i].firstChild.nodeValue))

permutation = []
remainder_of_cistrome = []

print "\nnumber of shared red posts is " + str(nred_shared)
print "number of shared white posts is " + str(nwhite_shared)

for i in range(len(param_name)):

	print "\nCistrome " + str(i) 

	print "number of unshared red posts is " + str(int(param_nred_unshared[i].firstChild.nodeValue))
	print "number of unshared white posts is " + str(int(param_nwhite_unshared[i].firstChild.nodeValue))
      
resultpath = (param_resultpath[0].firstChild.nodeValue)
print "\nResults will be written to " + (param_resultpath[0].firstChild.nodeValue)

max_num_tfbs = (int(param_maxtfbs[0].firstChild.nodeValue))
print "Maximum number of TFBS per segment is " + str(int(param_maxtfbs[0].firstChild.nodeValue))

# first of all we want to assign the positions of the shared posts...

permutation = numpy.random.permutation(nseg)

print "\nCistrome looks like this " + str(permutation) + "\n"
print "Shared segments \n"

# these posts are shared between all cistromes
shared_reds = permutation[0:nred_shared]
shared_whites = permutation[nred_shared:nwhite_shared+nred_shared]

print "The following segments are shared reds :" + str(shared_reds) + "\n"
print "The following segments are shared whites :" + str(shared_whites) + "\n"

remainder_of_cistrome = permutation[nwhite_shared+nred_shared:nseg]
permutation = remainder_of_cistrome

print "The remainder of the cistrome is :" + str(permutation) + "\n"

# we need to do this for each cistrome

for c in range(len(name)):

	print "Compiling cistrome " + str(c) + " " + name[c]

	print "\nCistrome " + str(c) + " currently looks like :" + str(permutation) + " \n"

	# first permute the remaining unshared segments so that
	# they aren't just a duplicate of the first cistrome

	unshared_reds = permutation[0:nred_unshared[c]]
	unshared_whites = permutation[nred_unshared[c]:nwhite_unshared[c]+nred_unshared[c]]

	print "The following segments are unshared reds :" + str(unshared_reds) + "\n"
	print "The following segments are unshared whites :" + str(unshared_whites) + "\n"

	remainder_of_cistrome = remainder_of_cistrome[nwhite_unshared[c]+nred_unshared[c]:nseg]
	print "Remainder of Cistrome looks like this " + str(remainder_of_cistrome) + "\n"

	permutation=remainder_of_cistrome

	# open an XML file for the generated cistrome data

	cistrome = 'overlapped_cistrome_' + name[c] + '.xml'

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
	xml.write(str(name[c])),
	xml.write("</name>")
        xml.write("\n")
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

	this_unshared_reds = unshared_reds[c]
	this_unshared_whites = unshared_whites[c]

	reds = numpy.concatenate((shared_reds, unshared_reds))
	whites = numpy.concatenate((shared_whites, unshared_whites))

	x = int(x)
	y = int(y)

	nred_unshared[c] = float(nred_unshared[c])
	nwhite_unshared[c] = float(nwhite_unshared[c])

	for p in range(y):

		for q in range(x):

			num_of_segment = (q * x) + p

			num_tfs = 0
			product = ['none']

			# if num_of_segment is in array posts then segment is a post

			if num_of_segment in whites:

				num_tfs = random.randint(1,max_num_tfbs)
				product = ['none']

			# if num_of_segment is in array reds then segment is red

			if num_of_segment in reds:

				num_tfs = random.randint(1,max_num_tfbs)
				product = ['tf']

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
