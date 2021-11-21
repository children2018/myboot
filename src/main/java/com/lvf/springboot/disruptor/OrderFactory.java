package com.lvf.springboot.disruptor;

import com.lmax.disruptor.EventFactory;

public class OrderFactory implements EventFactory{

    @Override
    public Object newInstance() {

        //System.out.println("OrderFactory.newInstance");
        return new Order();
    }

}