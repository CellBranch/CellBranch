pdf("two_cistrome_boxplot_grid.pdf", bg="white");

data <- read.csv("n4_o3/200/final_step_counts", sep=" ");

time1 <- data[[1]];

n1_reds <- data[[2]];
n1_shared_reds <- data[[6]];

o1_reds <- data[[8]];
o1_shared_reds <- data[[12]];

n1_prop_active <- 100 * (n1_reds + n1_shared_reds) / 631;
o1_prop_active <- 100 * (o1_reds + o1_shared_reds) / 466;

data <- read.csv("n4_o5/200/final_step_counts", sep=" ");

time2 <- data[[1]];

n2_reds <- data[[2]];
n2_shared_reds <- data[[6]];

o2_reds <- data[[8]];
o2_shared_reds <- data[[12]];

n2_prop_active <- 100 * (n2_reds + n2_shared_reds) / 631;
o2_prop_active <- 100 * (o2_reds + o2_shared_reds) / 466;

data <- read.csv("n4_o6/200/final_step_counts", sep=" ");

time3 <- data[[1]];

n3_reds <- data[[2]];
n3_shared_reds <- data[[6]];

o3_reds <- data[[8]];
o3_shared_reds <- data[[12]];

n3_prop_active <- 100 * (n3_reds + n3_shared_reds) / 631;
o3_prop_active <- 100 * (o3_reds + o3_shared_reds) / 466;

data <- read.csv("n4_o12/200/final_step_counts", sep=" ");

time4 <- data[[1]];

n4_reds <- data[[2]];
n4_shared_reds <- data[[6]];

o4_reds <- data[[8]];
o4_shared_reds <- data[[12]];

n4_prop_active <- 100 * (n4_reds + n4_shared_reds) / 631;
o4_prop_active <- 100 * (o4_reds + o4_shared_reds) / 466;

data <- read.csv("n7_o3/200/final_step_counts", sep=" ");

time5 <- data[[1]];

n5_reds <- data[[2]];
n5_shared_reds <- data[[6]];

o5_reds <- data[[8]];
o5_shared_reds <- data[[12]];

n5_prop_active <- 100 * (n5_reds + n5_shared_reds) / 631;
o5_prop_active <- 100 * (o5_reds + o5_shared_reds) / 466;

data <- read.csv("n7_o5/200/final_step_counts", sep=" ");

time6 <- data[[1]];

n6_reds <- data[[2]];
n6_shared_reds <- data[[6]];

o6_reds <- data[[8]];
o6_shared_reds <- data[[12]];

n6_prop_active <- 100 * (n6_reds + n6_shared_reds) / 631;
o6_prop_active <- 100 * (o6_reds + o6_shared_reds) / 466;

data <- read.csv("n7_o6/200/final_step_counts", sep=" ");

time7 <- data[[1]];

n7_reds <- data[[2]];
n7_shared_reds <- data[[6]];

o7_reds <- data[[8]];
o7_shared_reds <- data[[12]];

n7_prop_active <- 100 * (n7_reds + n7_shared_reds) / 631;
o7_prop_active <- 100 * (o7_reds + o7_shared_reds) / 466;

data <- read.csv("n7_o12/200/final_step_counts", sep=" ");

time8 <- data[[1]];

n8_reds <- data[[2]];
n8_shared_reds <- data[[6]];

o8_reds <- data[[8]];
o8_shared_reds <- data[[12]];

n8_prop_active <- 100 * (n8_reds + n8_shared_reds) / 631;
o8_prop_active <- 100 * (o8_reds + o8_shared_reds) / 466;

data <- read.csv("n8_o3/200/final_step_counts", sep=" ");

time9 <- data[[1]];

n9_reds <- data[[2]];
n9_shared_reds <- data[[6]];

o9_reds <- data[[8]];
o9_shared_reds <- data[[12]];

n9_prop_active <- 100 * (n9_reds + n9_shared_reds) / 631;
o9_prop_active <- 100 * (o9_reds + o9_shared_reds) / 466;

data <- read.csv("n8_o5/200/final_step_counts", sep=" ");

time10 <- data[[1]];

n10_reds <- data[[2]];
n10_shared_reds <- data[[6]];

o10_reds <- data[[8]];
o10_shared_reds <- data[[12]];

n10_prop_active <- 100 * (n10_reds + n10_shared_reds) / 631;
o10_prop_active <- 100 * (o10_reds + o10_shared_reds) / 466;

data <- read.csv("n8_o6/200/final_step_counts", sep=" ");

time11 <- data[[1]];

n11_reds <- data[[2]];
n11_shared_reds <- data[[6]];

o11_reds <- data[[8]];
o11_shared_reds <- data[[12]];

n11_prop_active <- 100 * (n11_reds + n11_shared_reds) / 631;
o11_prop_active <- 100 * (o11_reds + o11_shared_reds) / 466;

data <- read.csv("n8_o12/200/final_step_counts", sep=" ");

time12 <- data[[1]];

n12_reds <- data[[2]];
n12_shared_reds <- data[[6]];

o12_reds <- data[[8]];
o12_shared_reds <- data[[12]];

n12_prop_active <- 100 * (n12_reds + n12_shared_reds) / 631;
o12_prop_active <- 100 * (o12_reds + o12_shared_reds) / 466;

data <- read.csv("n16_o3/200/final_step_counts", sep=" ");

time13 <- data[[1]];

n13_reds <- data[[2]];
n13_shared_reds <- data[[6]];

o13_reds <- data[[8]];
o13_shared_reds <- data[[12]];

n13_prop_active <- 100 * (n13_reds + n13_shared_reds) / 631;
o13_prop_active <- 100 * (o13_reds + o13_shared_reds) / 466;

data <- read.csv("n16_o5/200/final_step_counts", sep=" ");

time14 <- data[[1]];

n14_reds <- data[[2]];
n14_shared_reds <- data[[6]];

o14_reds <- data[[8]];
o14_shared_reds <- data[[12]];

n14_prop_active <- 100 * (n14_reds + n14_shared_reds) / 631;
o14_prop_active <- 100 * (o14_reds + o14_shared_reds) / 466;

data <- read.csv("n16_o6/200/final_step_counts", sep=" ");

time15 <- data[[1]];

n15_reds <- data[[2]];
n15_shared_reds <- data[[6]];

o15_reds <- data[[8]];
o15_shared_reds <- data[[12]];

n15_prop_active <- 100 * (n15_reds + n15_shared_reds) / 631;
o15_prop_active <- 100 * (o15_reds + o15_shared_reds) / 466;

data <- read.csv("n16_o12/200/final_step_counts", sep=" ");

time16 <- data[[1]];

n16_reds <- data[[2]];
n16_shared_reds <- data[[6]];

o16_reds <- data[[8]];
o16_shared_reds <- data[[12]];

n16_prop_active <- 100 * (n16_reds + n16_shared_reds) / 631;
o16_prop_active <- 100 * (o16_reds + o16_shared_reds) / 466;

par(mfrow = c(4,4));

boxplot(n1_prop_active, o1_prop_active, notch=FALSE, axes=FALSE, main="nanog m=0.5 * mcrit \n oct4 m=0.5 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n2_prop_active, o2_prop_active, notch=FALSE, axes=FALSE, main="nanog m=0.5 * mcrit \n oct4 m=mcrit - 1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n3_prop_active, o3_prop_active, notch=FALSE, axes=FALSE, main="nanog m=0.5 * mcrit \n oct4 m=mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n4_prop_active, o4_prop_active, notch=FALSE, axes=FALSE, main="nanog m=0.5 * mcrit \n oct4 m=2 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n5_prop_active, o5_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit - 1 \n oct4 m=0.5 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n6_prop_active, o6_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit - 1 \n oct4 m=mcrit - 1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n7_prop_active, o7_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit - 1 \n oct4 m=mcrit+1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n8_prop_active, o8_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit - 1\n oct4 m=mcrit+1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n9_prop_active, o9_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit \n oct4 m=0.5 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n10_prop_active, o10_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit \n oct4 m=mcrit - 1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n11_prop_active, o11_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit \n oct4 m=mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n12_prop_active, o12_prop_active, notch=FALSE, axes=FALSE, main="nanog m=mcrit \n oct4 m=2 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n13_prop_active, o13_prop_active, notch=FALSE, axes=FALSE, main="nanog m=2 * mcrit \n oct4 m=0.5 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n14_prop_active, o14_prop_active, notch=FALSE, axes=FALSE, main="nanog m=2 * mcrit \n oct4 m=mcrit - 1", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n15_prop_active, o15_prop_active, notch=FALSE, axes=FALSE, main="nanog m=2 * mcrit \n oct4 m=mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

boxplot(n16_prop_active, o16_prop_active, notch=FALSE, axes=FALSE, main="nanog m=2 * mcrit \n oct4 m=2 * mcrit", xlab= "Transcription Factor", ylab = "% activated reds", border=c("red","blue"), ylim=c(0,100));
axis(1, at=c(1,2), labels=c("nanog","oct4"));
axis(2,at=seq(0,100,10), labels=seq(0,100,10));
#axis(2,at=seq(0,100,10), labels=seq("0","10","20","30","40","50","60","70","80","90","100"));

mtext("percentage saturation with m", outer = TRUE);

dev.off();
