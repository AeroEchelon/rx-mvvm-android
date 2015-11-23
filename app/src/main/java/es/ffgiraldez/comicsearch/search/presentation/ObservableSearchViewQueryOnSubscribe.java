/*
 * Copyright (C) 2015 Fernando Franco Giráldez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.ffgiraldez.comicsearch.search.presentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;

public class ObservableSearchViewQueryOnSubscribe implements Observable.OnSubscribe<String> {
    private final ObservableSearchViewModel view;

    public ObservableSearchViewQueryOnSubscribe(ObservableSearchViewModel view) {
        this.view = view;
    }

    @Override
    public void call(final Subscriber<? super String> subscriber) {
        final PropertyChangeListener changeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (!subscriber.isUnsubscribed() &&
                        ObservableSearchViewModel.Property.QUERY.name().equals(event.getPropertyName())) {
                    subscriber.onNext((String) event.getNewValue());
                }
            }
        };

        view.addPropertyChangeListener(changeListener);
        subscriber.add(BooleanSubscription.create(new Action0() {
            @Override
            public void call() {
                view.removePropertyChangeListener(changeListener);
            }
        }));
        subscriber.onNext(view.getQuery());
    }
}
