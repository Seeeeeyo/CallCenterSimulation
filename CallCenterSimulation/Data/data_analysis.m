%% Data analysis: The average waiting time of customers
rng('default');

%reads the files
T1 = readtable('outputRun1Conf1.xlsx','Range','A:F');
T2 = readtable('outputRun2Conf1.xlsx','Range','A:F');
T3 = readtable('outputRun3Conf1.xlsx','Range','A:F');
T4 = readtable('outputRun4Conf1.xlsx','Range','A:F');
T5 = readtable('outputRun5Conf1.xlsx','Range','A:F');

runs = 5; %number of simulations considered

% conversion from table to matrix
matData = {};
corporates = [];
mat1 = str2double(table2array(T1(7:end,:)));
corporates(1) = find(mat1(:,1)==1,1,'last'); %finding the index of the first corporate arrival
matData{1} = mat1;
mat2 = str2double((table2array(T2(7:end,:))));
corporates(2) = find(mat2(:,1)==1,1,'last');
matData{2} = mat2;
mat3 = str2double((table2array(T3(7:end,:))));
corporates(3) = find(mat3(:,1)==1,1,'last');
matData{3} = mat3;
mat4 = str2double(table2array(T4(7:end,:)));
corporates(4) = find(mat4(:,1)==1,1,'last');
matData{4} = mat4;
mat5 = str2double((table2array(T5(7:end,:))));
corporates(5) = find(mat5(:,1)==1,1,'last');
matData{5} = mat5;

%% Extracting data from matrices


% waiting time of consumers
wcons = {};
%arrivals of consumers
arrcons = {};
% waiting time of corporates
wcorp = {};
%arrivals of corporates
arrcorp = {};
for i=1:runs
    mat = matData{i};
    wcons{i} = [mat(1:(corporates(i)-1),4,:)- mat(1:(corporates(i)-1),6), mat(1:(corporates(i)-1),5,:)];
    wcorp{i} = [mat((corporates(i)-1):end,4,:)- mat((corporates(i)-1):end,6),  mat((corporates(i)-1):end,5,:)];
    interarrivals = diff(mat((corporates(i)-1):end,2,:));
    interarrivals(interarrivals<0) =0;
    arrcorp{i} = [mat((corporates(i)-1):end,2,:), [0; interarrivals]];
    times = mat(1:(corporates(i)-1),2,:);
    times = sort(times);
    arrcons{i} = [times, [diff(times);0]];
end

%% merging waiting time of consumers and waiting time of corporates into a waiting time of all customers
w = {};

figure;
% merge wcs and wcr to w
for i=1: runs
    m1 = wcons{i};
    m2 = wcorp{i};
    newMat = [m1;m2];
    newMat= toUnique(sortrows(newMat));
     
    plot(convertTime(newMat(:,1)),newMat(:,2));
    hold on
    w{i} = newMat;
    
    wcons{i} = toUnique(sortrows(m1));
    wcorp{i} = toUnique(sortrows(m2));
end
hold off
%% averaging the simulations

% set the min and max x values of all the runs the be equivalent, if not
% done, interpolation will build more extreme values


%find the steady state. first step: find the thresold using welsh approach:
%average out the simulations

max_size = 24*60*60; %period of 24h
precision = 2;
ycust = zeros(max_size*2,runs);
ycons = zeros(max_size*2,runs);
ycorp = zeros(max_size*2,runs);
xicust = linspace(0, max_size, max_size*precision);
xicorp = linspace(0, max_size, max_size*precision);
xicons = linspace(0, max_size, max_size*precision);
for i=1:runs
    wp = wcorp{i};
    ws = wcons{i};
    wt = w{i};
ycust(:,i) = interp1(wt(:,1),wt(:,2), xicust(:)', 'linear');%interpolation
ycons(:,i) = interp1(ws(:,1),ws(:,2), xicons(:)', 'linear');%interpolation
ycorp(:,i) = interp1(wp(:,1),wp(:,2), xicorp(:)', 'linear');%interpolation
end

ycust_mean = mean(ycust, 2);   %computing the mean of all the simulations data points
ycust_mean(find(isnan(ycust_mean)))=0;
ycorp_mean = mean(ycorp, 2);   %computing the mean of all the simulations data points
ycorp_mean(find(isnan(ycorp_mean)))=0;
ycons_mean = mean(ycons, 2);   %computing the mean of all the simulations data points
ycons_mean(find(isnan(ycons_mean)))=0;

nan_indices = find(isnan(ycust_mean));
xicust(nan_indices)=[];
ycust_mean(nan_indices)=[];

nan_indices = find(isnan(ycons_mean));
xicons(nan_indices)=[];
ycons_mean(nan_indices)=[];

nan_indices = find(isnan(ycorp_mean));
xicorp(nan_indices)=[];
ycorp_mean(nan_indices)=[];

xseccust = hours(seconds(xicust(:)));
xseccorp = hours(seconds(xicorp(:)));
xseccons = hours(seconds(xicons(:)));

figure;plot(convertTime(xseccust),ycust_mean,'-r');
figure;plot(convertTime(xseccorp),ycorp_mean,'-r');
figure;plot(convertTime(xseccons),ycons_mean,'-r');

%% Data analysis of Corporates: arrivals

arrival = arrcorp{1};
arrivaltime =arrival(:,1);
range = (max(arrivaltime)-min(arrivaltime))/3600;
interarrival=arrival(:,2);
 figure; plot(convertTime(arrivaltime),interarrival);
 figure; histogram(arrivaltime,ceil(range))

hold on 
x = [2 12];
y = [1 1];
plot((x*60*60),y*60,'r')
x = [0 2];
y = [0.2 0.2];
plot((x*60*60),y*60,'r')
x = [12 24];
y = [0.2 0.2];
plot((x*60*60),y*60,'r')

%% Delays analysis of Corporates

consdel = wcorp{4};
consdel = sortrows(consdel);
% consdelays = ycorp_mean;
consdelays = consdel(:,2);
consdelays(consdelays==0)=[]; %having zeros in the data will make the computations of the exp and gamma mle impossible
n = length(consdelays);
delaytimes = consdel(:,1);

figure;
subplot(1,3,1); title('boxplot'); boxplot(consdelays)
xs2 = 1:1:n;
subplot(1,3,2); title('scatter plot'); scatter(xs2,consdelays)
subplot(1,3,3);  h = histogram(consdelays,10); title('histogram');
hold on
% stat analysis
u = mean(consdelays)
sigma = std(consdelays)
v = var(consdelays)
range = max(consdelays) - min(consdelays)

% by looking at the data, we can guess that we can an exponantial or a
% gamma distribution (with alpha<1) might fit the distribution.
figure;
histogram(consdelays,'Normalization','pdf'); title('histogram');
hold on
%MLE calculation of B_hat for exponantial is the mean of the data
b_hat = u;

expPDF = @(x) exp(-x/b_hat)/b_hat;
plot(xs2,expPDF(xs2),'r')

% let's now apply a chi squared test to test our hypothesis:
binsNb = h.NumBins;
bins = h.BinEdges;
N = h.Values;
chivalue =0;
% chi-square algorithm
for i=1: binsNb-1;
    prob = expcdf(bins(i+1),b_hat)- expcdf(bins(i),b_hat);
    expected = prob*n;
    observed = N(i);
      add = ((observed-expected).^2)/expected;
    chivalue = chivalue+ add;
end
chivalue

% chiSquare (7.6272) is smaller than 16.92 so we have no reason to reject the null
% hypothesis. Therefore, the data could come from a gamma distribution with
% the alpha and beta defined above.

% %MLE estimation of the alpha and beta parameters:
% % the mle
% fun = @(x) (log(u./x) + psi(x) - sum(log(delays))./n);
% 
% mlex = 0:0.01:5;
% % figure; plot(mlex,fun(mlex))
% alpha = fzero(fun,[1 6])
% beta = u/alpha
% 
% gammaPDF =@(x) exp(-x/beta).*x.^(alpha-1).*beta.^(-alpha)/gamma(alpha);
% plot(xs2,gammaPDF(xs2),'b')
% 
% for i=1: binsNb-1;
%     pi = gamcdf(bins(i+1),alpha,beta)- gamcdf(bins(i),alpha,beta);
%     expected = pi*n;
%     observed = N(i);
%       add = ((observed-expected).^2)/expected;
%     chivalue = chivalue+ add;
% end
% chivalue
% 
% % chiSquare is larger than 16.92 so we have to reject the null
% % hypothesis. Therefore, the data does ont come from a gamma distribution with
% % the alpha and beta defined above. Probably, no distribution fits the data

%% Data analysis of Consumers: arrivals

arrival = arrcons{1};
arrivaltime =arrival(:,1);
range = (max(arrivaltime)-min(arrivaltime))/3600;
interarrival=arrival(:,2);
figure; plot(convertTime(arrivaltime),interarrival);
figure; histogram(arrivaltime,ceil(range))

hold on 
height = (2 - 0.2)*60;
transX = (6-9) * 60 * 60;
transY = 2*60;
period = 24 *60*60;
sinusoid = @(x) transY + height*sin(2 * pi * (x + transX) / period);
plot(arrivaltime,sinusoid(arrivaltime),'r');
hold off
%% Delays analysis of Consumers

consdel = wcons{4};
consdel = sortrows(consdel);
consdelays = consdel(:,2);
%   consdelays = ycons_mean;
consdelays(consdelays==0)=[]; %having zeros in the data will make the computations of the exp and gamma mle impossible
n = size(consdelays,1);
delaytimes = consdel(:,1);

figure;
subplot(1,3,1); title('boxplot'); boxplot(consdelays)
xs2 = 1:1:n;
subplot(1,3,2); title('scatter plot'); scatter(xs2,consdelays)
subplot(1,3,3);  h = histogram(consdelays,10); title('histogram');
hold on
% stat analysis
u = mean(consdelays)
sigma = std(consdelays)
v = var(consdelays)
range = max(consdelays) - min(consdelays)

% by looking at the data, we can guess that we can an exponantial or a
% gamma distribution (with alpha<1) might fit the distribution.
figure;
histogram(consdelays,'Normalization','pdf'); title('histogram');
hold on
%MLE calculation of B_hat for exponantial is the mean of the data
b_hat = u;

expPDF = @(x) exp(-x/b_hat)/b_hat;
plot(xs2,expPDF(xs2),'r')

% let's now apply a chi squared test to test our hypothesis:
binsNb = h.NumBins;
bins = h.BinEdges;
N = h.Values;
chivalue =0;
% chi-square algorithm
for i=1: binsNb-1;
    prob = expcdf(bins(i+1),b_hat)- expcdf(bins(i),b_hat);
    expected = prob*n;
    observed = N(i);
      add = ((observed-expected).^2)/expected;
    chivalue = chivalue+ add;
end
chivalue

% the chi value ( 327.6615) is larger than 16.92 and is unfortunately way too high to be considered for a=0.05
% and thus the null hypothesis must be rejected. Our new hypothesis is that
% the a gamma distribution fits the data.

%MLE estimation of the alpha and beta parameters:
% the mle
funcons= @(x) (log(u./x) + psi(x) - sum(log(consdelays))./n);

mlex = 0:0.01:5;
%  figure; plot(mlex,fun(mlex))
alpha = fzero(funcons,[0.001 6])
beta = u/alpha

gammaPDF =@(x) exp(-x/beta).*x.^(alpha-1).*beta.^(-alpha)/gamma(alpha);
plot(xs2,gammaPDF(xs2),'b')

for i=1: binsNb-1;
    pi = gamcdf(bins(i+1),alpha,beta)- gamcdf(bins(i),alpha,beta);
    expected = pi*n;
    observed = N(i);
      add = ((observed-expected).^2)/expected;
    chivalue = chivalue+ add;
end
chivalue

% chiSquare is larger than 16.92 so we have to reject the null
% hypothesis. Therefore, the data does ont come from a gamma distribution with
% the alpha and beta defined above. Probably, no distribution fits the data
hold off

%% Find the steady state of the data

lowpass_strength = 1000;
wsmoothed = lowpassfilter(ycust_mean,lowpass_strength);
plot(xseccust,wsmoothed,'-b');
hold off

% find graphically the threshold:
l = 2.2*60*60;

means = zeros(1,runs);
confidenceInt = zeros(1,2);

% go through each run
for i=1:runs
    ycons_mean = w{i};
    thresh = find(ycons_mean(:,1)>l);

    % remove the transient state
    ycons_mean = ycons_mean(thresh:size(ycons_mean,1),:);
    means(i) = mean(ycons_mean(:,2));
end
% mean of the means, center the condidence interval
Xbar = mean(means); 
alpha = 0.05;
dof = runs-1;
% The t critical value for dof degrees of freedom with an alpha of 0.5:
t = tinv(1-alpha/2,dof);

% calculate the confidence interval
bound = t*sqrt(var(means)/length(means));
confidenceInt(1) = Xbar-bound;
confidenceInt(2) = Xbar+bound

%% percentage of customers exceeding the performance bounds

guarantee = [true true true true];
% performance guarantees:
%90'%' of the consumers should be assisted within 5 minutes; 
guarantee(1) = (90/100 <= length(ycons_mean(ycons_mean<=5*60))/length(ycons_mean));

%95'%' within 10 minutes
guarantee(2) = (95/100 <= length(ycons_mean(ycons_mean<=10*60))/length(ycons_mean));

%For corporate clients, 95'%' should be assisted within 3 minutes
guarantee(3) = (95/100 <= length(ycorp_mean(ycorp_mean<=5*60))/length(ycorp_mean));
%99'%' within 7 minutes. 
guarantee(4) = (99/100 <=length(ycorp_mean(ycorp_mean<=5*60))/length(ycorp_mean));
guarantee
%%

 function out = interarrivalfrequ(data,strength)
     out = zeros(ceil(max(data)),1);
     for i=1: ceil(max(data))
         boundlow = i-strength;
         boundhigh = i+strength;

         length(find((data<boundhigh) - (boundlow<data)==0));
         out(i) = length(find((data<boundhigh)-(boundlow<data)==0));
     end
 end
     
function out = lowpassfilter(data,strength)
out = zeros(length(data),1);
for i=1: length(data)
    boundlow = i-strength;
    if(boundlow<1)
        boundlow = 1;
    end 
     boundhigh = i+strength;
    if(boundhigh>length(data))
        boundhigh = length(data);
    end
    data(boundlow:boundhigh);
    out(i) = mean(data(boundlow:boundhigh));
end
end
%-------------------------------------------------------------------   
function out = toUnique(newMat)
[values, ~, ids] = unique(newMat(:,1), 'rows');  
    out = splitapply(@(rows) sum(rows,1), newMat, ids); 
    out(:,1) = values;
end

function out = convertTime(array)
%    out = array+6*60*60;
%    out(out>24*60*60) = out(out>24*60*60)-24*60*60;
  out = hours(seconds(array));
end

% max_size = 0;
% for i=1:runs
%     ixs = xs{i};
%     iys = ys{i};
% 
% %     if(ixs(1)>minx)
% %         ixs = [minx; ixs];
% %         iys = [iys(1); iys];
% %     end
% %     if(ixs(length(ixs))<maxx)
% %         ixs(length(ixs)+1) = maxx;
% %         iys(length(iys)+1) =  iys(length(iys));
% %     end
% 
%     if(length(ixs)>max_size)
%         max_size = length(ixs);
%     end
%     xs{i} = ixs;
%     ys{i} = iys;
% end