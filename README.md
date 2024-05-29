### 仿知乎APP

#### Day1:
写了一堆Fragment，没有具体业务逻辑
	
#### Day2：
完成主页：
ViewPager2+BottomNavigationView+Fragment+TabLayout+Fragment+ViewPager2嵌套 (静态页面)
	
#### Day3：
完成推荐页面：
推荐页面里面还要嵌套TabLayout+ViewPager2+Fragment，绕晕了(静态页面)

![image-20240529155904174](C:\Users\wellorbetter\AppData\Roaming\Typora\typora-user-images\image-20240529155904174.png)

但是存在bug，不过现在没有写数据库，也还行，等以后改(切换fragment的时候会出现空指针，应该是重用的时候出错了)

然后就是TabLayout，如果是小于五个，就算写成scrollable也是不能滑动的，有可能是写的问题，不懂(知乎上面那个是可以滑动的)

下面的图标有点问题，点击的时候颜色太淡了，得换成点击变深色

#### Day4：
完成部分注册页面：
画UI实在是麻烦(静态页面)

![image-20240529154718156](C:\Users\wellorbetter\AppData\Roaming\Typora\typora-user-images\image-20240529154718156.png)

虽然麻烦，但是画UI不需要动脑子，相对来说比较轻松