 %y = betarnd(2,4,100,1);
 %x = rand(100,1);
 %XY = [x y];
 
 %plot(XY(:,1),XY(:,2),'*');
 
 

%colors
C = {'b','r','g','y',[.3 .3 .3], 'k'};
 
clf
axis equal
hold on
x1=600;
y1=600;
rc=500;
[x,y,z] = cylinder(rc,200);
plot(x(1,:)+x1,y(1,:)+y1,':k')

teamcnt = 5;
membercnt = 1000;
leadercnt = 50;
othercnt = 300;

%configlines = repmat('%s\t',1,membercnt+leadercnt+othercnt);
offset = 0;
f = fopen('component.cfg', 'w');

for t=1:teamcnt
    [x y]=points_in_circle(x1,y1,rc, membercnt/teamcnt);
    plot(x,y,'color',C{t},'marker','x','line','none')
    for i=1:membercnt/teamcnt
        fprintf(f, 'M M%u T%u %u %u\n', offset+i, t, round(x(i)), round(y(i)));
    end
    offset = offset + membercnt/teamcnt;
end

offset = 0;
for t=1:teamcnt
    [x y]=points_in_circle(x1,y1,rc, leadercnt/teamcnt);
    plot(x,y,'color',C{t},'marker','o','line','none')
    for i=1:leadercnt/teamcnt
        fprintf(f, 'L L%u T%u %u %u\n', offset+i, t, round(x(i)), round(y(i)));
    end
    offset = offset + leadercnt/teamcnt;
end

[x y]=points_in_circle(x1,y1,rc+50, othercnt);
plot(x,y,'color',[.9 .9 .9],'marker','*','line','none')
for i=1:othercnt
    fprintf(f, 'O O%u %u %u\n', i, round(x(i)), round(y(i)));
end

fclose(f);

