package com.lovejjfg.circle.anim.factory;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.lovejjfg.circle.anim.particle.Particle;


/**
 * Created by Administrator on 2015/11/29 0029.
 */
public abstract class ParticleFactory {
    public abstract Particle[][] generateParticles(Bitmap bitmap, Rect bound);
}
