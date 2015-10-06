pdf("three_cistrome_run1_grid.pdf", bg="white");

data <- read.csv("m0/200/0/1/simOutputData.csv", sep="");

time1 <- data[[1]];

n1_reds <- data[[2]];
n1_shared_reds <- data[[6]];

o1_reds <- data[[8]];
o1_shared_reds <- data[[12]];

s1_reds <- data[[14]];
s1_shared_reds <- data[[18]];

n1_prop_active <- 100 * (n1_reds + n1_shared_reds) / 631;
o1_prop_active <- 100 * (o1_reds + o1_shared_reds) / 466;
s1_prop_active <- 100 * (s1_reds + s1_shared_reds) / 542;

data <- read.csv("m1/200/1/1/simOutputData.csv", sep="");

time2 <- data[[1]];

n2_reds <- data[[2]];
n2_shared_reds <- data[[6]];

o2_reds <- data[[8]];
o2_shared_reds <- data[[12]];

s2_reds <- data[[14]];
s2_shared_reds <- data[[18]];

n2_prop_active <- 100 * (n2_reds + n2_shared_reds) / 631;
o2_prop_active <- 100 * (o2_reds + o2_shared_reds) / 466;
s2_prop_active <- 100 * (s2_reds + s2_shared_reds) / 542;

data <- read.csv("m2/200/2/1/simOutputData.csv", sep="");

time3 <- data[[1]];

n3_reds <- data[[2]];
n3_shared_reds <- data[[6]];

o3_reds <- data[[8]];
o3_shared_reds <- data[[12]];

s3_reds <- data[[14]];
s3_shared_reds <- data[[18]];

n3_prop_active <- 100 * (n3_reds + n3_shared_reds) / 631;
o3_prop_active <- 100 * (o3_reds + o3_shared_reds) / 466;
s3_prop_active <- 100 * (s3_reds + s3_shared_reds) / 542;

data <- read.csv("m3/200/3/1/simOutputData.csv", sep="");

time4 <- data[[1]];

n4_reds <- data[[2]];
n4_shared_reds <- data[[6]];

o4_reds <- data[[8]];
o4_shared_reds <- data[[12]];

s4_reds <- data[[14]];
s4_shared_reds <- data[[18]];

n4_prop_active <- 100 * (n4_reds + n4_shared_reds) / 631;
o4_prop_active <- 100 * (o4_reds + o4_shared_reds) / 466;
s4_prop_active <- 100 * (s4_reds + s4_shared_reds) / 542;

data <- read.csv("m4/200/4/1/simOutputData.csv", sep="");

time5 <- data[[1]];

n5_reds <- data[[2]];
n5_shared_reds <- data[[6]];

o5_reds <- data[[8]];
o5_shared_reds <- data[[12]];

s5_reds <- data[[14]];
s5_shared_reds <- data[[18]];

n5_prop_active <- 100 * (n5_reds + n5_shared_reds) / 631;
o5_prop_active <- 100 * (o5_reds + o5_shared_reds) / 466;
s5_prop_active <- 100 * (s5_reds + s5_shared_reds) / 542;

data <- read.csv("m5/200/5/1/simOutputData.csv", sep="");

time6 <- data[[1]];

n6_reds <- data[[2]];
n6_shared_reds <- data[[6]];

o6_reds <- data[[8]];
o6_shared_reds <- data[[12]];

s6_reds <- data[[14]];
s6_shared_reds <- data[[18]];

n6_prop_active <- 100 * (n6_reds + n6_shared_reds) / 631;
o6_prop_active <- 100 * (o6_reds + o6_shared_reds) / 466;
s6_prop_active <- 100 * (s6_reds + s6_shared_reds) / 542;

data <- read.csv("m6/200/6/1/simOutputData.csv", sep="");

time7 <- data[[1]];

n7_reds <- data[[2]];
n7_shared_reds <- data[[6]];

o7_reds <- data[[8]];
o7_shared_reds <- data[[12]];

s7_reds <- data[[14]];
s7_shared_reds <- data[[18]];

n7_prop_active <- 100 * (n7_reds + n7_shared_reds) / 631;
o7_prop_active <- 100 * (o7_reds + o7_shared_reds) / 466;
s7_prop_active <- 100 * (s7_reds + s7_shared_reds) / 542;

data <- read.csv("m7/200/7/1/simOutputData.csv", sep="");

time8 <- data[[1]];

n8_reds <- data[[2]];
n8_shared_reds <- data[[6]];

o8_reds <- data[[8]];
o8_shared_reds <- data[[12]];

s8_reds <- data[[14]];
s8_shared_reds <- data[[18]];

n8_prop_active <- 100 * (n8_reds + n8_shared_reds) / 631;
o8_prop_active <- 100 * (o8_reds + o8_shared_reds) / 466;
s8_prop_active <- 100 * (s8_reds + s8_shared_reds) / 542;

par(mfrow = c(3,3));

plot(time1, n1_prop_active, type="l", main="nanog m=0", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time1, o1_prop_active, type="l", main="nanog m=0", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time1, s1_prop_active, type="l", main="nanog m=0", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time2, n2_prop_active, type="l", main="nanog m=1", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time2, o2_prop_active, type="l", main="nanog m=1", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time2, s2_prop_active, type="l", main="nanog m=1", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time3, n3_prop_active, type="l", main="nanog m=2", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time3, o3_prop_active, type="l", main="nanog m=2", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time3, s3_prop_active, type="l", main="nanog m=2", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time4, n4_prop_active, type="l", main="nanog m=3", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time4, o4_prop_active, type="l", main="nanog m=3", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time4, s4_prop_active, type="l", main="nanog m=3", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time5, n5_prop_active, type="l", main="nanog m=4", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time5, o5_prop_active, type="l", main="nanog m=4", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time5, s5_prop_active, type="l", main="nanog m=4", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time6, n6_prop_active, type="l", main="nanog m=5", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time6, o6_prop_active, type="l", main="nanog m=5", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time6, s6_prop_active, type="l", main="nanog m=5", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time7, n7_prop_active, type="l", main="nanog m=6", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time7, o7_prop_active, type="l", main="nanog m=6", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time7, s7_prop_active, type="l", main="nanog m=6", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time8, n8_prop_active, type="l", main="nanog m=7", xlab= "time", ylab = "% activated reds", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time8, o8_prop_active, type="l", main="nanog m=7", xlab= "time", ylab = "% activated reds", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
par(new=TRUE);
plot(time8, s8_prop_active, type="l", main="nanog m=7", xlab= "time", ylab = "% activated reds", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

mtext("percentage saturation with time", outer = TRUE);

dev.off();
