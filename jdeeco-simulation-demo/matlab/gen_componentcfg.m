 y = betarnd(2,4,100,1);
 x = rand(100,1);
 XY = [x y];
 
 plot(XY(:,1),XY(:,2),'*');
 
 
  %to test the function
clf
axis equal
hold on
x1=1
y1=1
rc=10
[x,y,z] = cylinder(rc,200);
plot(x(1,:)+x1,y(1,:)+y1,':k')


[x y]=points_in_circle(x1,y1,rc, 1000);
plot(x,y,'bx')

[x y]=points_in_circle(x1,y1,rc, 50);
plot(x,y,'or')


[x y]=points_in_circle(x1,y1,rc, 500);
plot(x,y,'*g')

