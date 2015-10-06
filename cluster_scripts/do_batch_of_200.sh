#!/bin/sh
#$ -cwd -V
#$ -N n8_o0
#$ -t 1-200
#$ -l h_rt=00:25:00
#$ -l h_vmem=4G
/opt/yarcc/infrastructure/java/1.7.0_60/1/default/bin/java -server -Xms300m -Xmx300m -classpath CBSimv1.0b.jar:mason.16.jar simv0.CBSimulation n8_o0_parameters.xml "${1}/${2}/${SGE_TASK_ID}"
date
