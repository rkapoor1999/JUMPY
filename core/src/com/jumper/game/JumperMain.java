package com.jumper.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class JumperMain extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bg;
	Texture[] enemy;
	Texture[] bird;
	Texture[] coin;
	Texture[] life;
	int width, height, birdState,pause, birdY, enemyCount,enemyState, coinCount, coinState, gameState;
	float gravity = 0.4f;
	float velocity = 0f;
	ArrayList<Integer> enemyX, enemyY, coinX, coinY;
	Random random;
	ArrayList<Rectangle> enemyRectangles, coinRectangles;
	Rectangle birdRect;
	int score;
	BitmapFont scoreFont;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Texture("Full-background.png");

		score =0;
		bird = new Texture[4];
		bird[0] = new Texture("frame-1.png");
		bird[1] = new Texture("frame-2.png");
		bird[2] = new Texture("frame-3.png");
		bird[3] = new Texture("frame-4.png");


		birdState = pause =0;
		birdY = 210;
		random = new Random();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		enemy = new Texture[2];
		enemy[0] = new Texture("enemy1.png");
		enemy[1] = new Texture("enemy2.png");

		enemyCount = 0;
		enemyState = 0;
		enemyX = new ArrayList<Integer>();
		enemyY = new ArrayList<Integer>();
		enemyRectangles = new ArrayList<Rectangle>();

		coinCount = 0;
		coinState = 0;
		coin = new Texture[10];
		coin[0] = new Texture("coin1.png");
        coin[1] = new Texture("coin2.png");
        coin[2] = new Texture("coin3.png");
        coin[3] = new Texture("coin4.png");
        coin[4] = new Texture("coin5.png");
        coin[5] = new Texture("coin6.png");
        coin[6] = new Texture("coin7.png");
        coin[7] = new Texture("coin8.png");
        coin[8] = new Texture("coin9.png");
        coin[9] = new Texture("coin10.png");
		coinRectangles = new ArrayList<Rectangle>();

		coinX = new ArrayList<Integer>();
		coinY = new ArrayList<Integer>();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(10);

		gameState = 0;

	}

	@Override
	public void render () {
		batch.begin();

		if(gameState ==0)
		{
			//new Game screen
			if(Gdx.input.justTouched())
			{
				gameState = 1;
			}

			if(pause<8)
			{
				pause++;
			}
			else {
				pause = 0;
				if(birdState<3)
				{
					birdState += 1;
				}
				else {
					birdState = 0;
				}
			}

			batch.draw(bg,0,0,width,height);
			scoreFont.draw(batch,"Click to start new game",100,height/2);
			batch.draw(bird[birdState],100,height/2);
		}
		else if(gameState == 1)
		{
			//game running
			boolean isTouched = Gdx.input.isTouched();
			if(isTouched)
			{
				velocity = -10;
			}


			if(pause<4)
			{
				pause++;
			}
			else {
				pause = 0;
				if(birdState <3)
				{
					birdState += 1;
				}
				else
				{
					birdState = 0;
				}
				if(enemyState <1)
				{
					enemyState++;
				}
				else {
					enemyState = 0;
				}
				if(coinState<9)
				{
					coinState++;
				}
				else {
					coinState = 0;
				}
			}
			if(enemyCount<250)
			{
				enemyCount++;
			}
			else {
				enemyCount = 0;
				makeEnemy();
			}

			if(coinCount<100)
			{
				coinCount++;
			}
			else {
				coinCount = 0;
				makeCoin();
			}


			velocity += gravity;
			birdY -= velocity;
			if(birdY <= 80)
			{
				birdY = 80;
			}
			if(birdY + 250 >= height)
			{
				birdY = height - 250;
			}

			batch.draw(bg,0,0,width,height);


			coinRectangles.clear();
			for(int i = 0;i<coinX.size();i++)
			{
				if(coinX.get(i)<0)
				{
					coinX.remove(i);
					coinY.remove(i);
				}
				else {
					batch.draw(coin[coinState],coinX.get(i),coinY.get(i),100,100);
					coinX.set(i,coinX.get(i)-5);
					coinRectangles.add(new Rectangle(coinX.get(i),coinY.get(i),100,100));
				}
			}

			enemyRectangles.clear();
			for(int i = 0;i<enemyX.size();i++)
			{
				if(enemyX.get(i)<0)
				{
					enemyX.remove(i);
					enemyY.remove(i);
				}
				else {
					batch.draw(enemy[enemyState],enemyX.get(i),enemyY.get(i),100,100);
					enemyX.set(i,enemyX.get(i)-5);
					enemyRectangles.add(new Rectangle(enemyX.get(i),enemyY.get(i),100,100));
				}
			}
			batch.draw(bird[birdState],100, birdY,250,300);

			birdRect = new Rectangle(105, birdY+5,200,250);

			for(int i=0;i<enemyRectangles.size();i++)
			{
				if(Intersector.overlaps(birdRect,enemyRectangles.get(i)))
				{
					Gdx.app.log("Enemy","Collided");
					enemyX.remove(i);
					enemyY.remove(i);
					break;
				}
			}

			for(int i =0;i<coinRectangles.size();i++)
			{
				if(Intersector.overlaps(birdRect,coinRectangles.get(i)))
				{
					Gdx.app.log("Coin","Collided");
					score++;
					coinX.remove(i);
					coinY.remove(i);
					break;
				}
			}

			scoreFont.draw(batch,String.valueOf(score),width/2,150);

		}
		else {
			//game Over
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();
		for(Texture t: bird)
			t.dispose();
		for(Texture t:enemy)
			t.dispose();
		for(Texture t:coin)
			t.dispose();
	}

	private void makeEnemy()
	{
		float height = random.nextFloat()*this.height;
		enemyY.add((int)height);
		enemyX.add(width);

	}

    private void makeCoin()
    {
        float height = random.nextFloat()*this.height;
        coinY.add((int)height);
        coinX.add(width);

    }

}
