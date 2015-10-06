#!/bin/bash

for dir in {0..50}
do
	cd $dir
	echo "Working in directory " $dir"/"

	echo "Collating numbers of activated reds in the last simulation step"
	tail --lines=1 */simOutputData.csv > final_count_reds
	more final_count_reds | grep -v 'simOutputData' > temp
	more temp | grep '[^0-9]' > temp2
	more temp2 | awk '{print $2;}' > final_count_reds
	rm temp temp2

	cd ..
done
