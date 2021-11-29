close all;
clc;
img=imread('24.jpg');
img(102,112,1)
l_avg=xlsread('Avg_3_f.xlsx');
i=imresize(img,[256 256]);% figure; imshow(i);
image=rgb2lab(i);
im3=rgb2gray(i);
im4=image(:,:,2);
im2=im3(64:191, 64:191);
im=im4(64:191, 64:191);figure; imshow(im);
level = graythresh(im);
BW = imbinarize(im,level);
s=size(BW);
count=0;figure; imshow(i);
lbp=zeros(1,16);
for i=1:s(2)
    for j=1:s(1)
        if(BW(i,j)==1) 
            count=count+1;
             byte=zeros(1,8);
            byte_cnt=1;
            if(i>1&&i<s(2)-1&&j>1&&j<s(1)-1)
                for a=-1:1
                    for b=-1:1
                        if(im2(i,j)>im2(i+a,j+b))
                            byte(byte_cnt)=1;
                        end;
                        if((a~=0)&&(b~=0))
                            byte_cnt=byte_cnt+1;
                        end;
                    end;
                end;
            end;
            lbp(bi2de(byte)+1)=lbp(bi2de(byte)+1)+1;
         end;
    end;
end;
lbp=lbp/count;
lbp_std=std(lbp);
for j=1:3
         cross(1,j)=corr2(lbp(1,:),l_avg(j,:));
end
   [v,ind]= max(cross(:,:));
    res=[v,ind];
    res(2)