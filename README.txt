This is a simple Puzzle Game created in Java that also demonstrates my Scrollable Display built on Java Swing.

I made this program with the intent of creating the scrollable GUI then decided to build the Puzzle Game to exhibit the display's functionality.
This was developed in the Spring of my Freshman Year at Carleton College (2012).

The object of the game is to put the numbers in descending order, i.e. larger numbers above smaller numbers.
To manipulate the numbers, any number may be clicked, and then the entire pile from that number up is flipped.
Each flip takes one move, and upon completion of every level, the player's remaining move count is incremented by one and a half times the completed level rounded up.

The ScrollDisplay works with a DrawFrame to call the "drawImage" and "handleClick" methods of classes implementing the ScrollableView interface.
The ScrollDisplay and DrawFrame classes make a very simple and usable base for a GUI that scrolls.
The scroll bar will auto-hide but reaper on mouseover and stay fully opaque while scrolling.
Scrolling can be done either by clicking and dragging the scroll bar or using the scroll wheel (or it's trackpad equivalent).