function [x, y]=points_in_circle(x1,y1,rc, cnt)

    function [tx, ty]=inner(x1,y1,rc)
        a=2*pi*rand;
        r=sqrt(rand);
        tx=(rc*r)*cos(a)+x1;
        ty=(rc*r)*sin(a)+y1;
    end
    x = zeros(cnt, 1);
    y = zeros(cnt, 1);
    for t=1:cnt %loop until doing cnt points inside the circle
        [tx, ty]=inner(x1,y1,rc);
        x(t) = tx;
        y(t) = ty;        
    end;
end
   