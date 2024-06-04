import React, { useState } from 'react';
import './style.scss'; // Estilo CSS para el componente

const TimeInput = () => {
    const [hours, setHours] = useState('');
    const [minutes, setMinutes] = useState('');

    const handleHoursChange = (event) => {
        const value = event.target.value;
        if (value === '' || (parseInt(value) >= 0 && parseInt(value) <= 23)) {
            setHours(value);
        }
    };

    const handleMinutesChange = (event) => {
        const value = event.target.value;
        if (value === '' || (parseInt(value) >= 0 && parseInt(value) <= 59)) {
            setMinutes(value);
        }
    };

    return (
        <div className="time-input">
            <input
                type="number"
                id="hours"
                value={hours}
                onChange={handleHoursChange}
                min="0"
                max="23"
                placeholder="HH"
            />
            <span>:</span>
            <input
                type="number"
                id="minutes"
                value={minutes}
                onChange={handleMinutesChange}
                min="0"
                max="59"
                placeholder="MM"
            />
        </div>
    );
};

export default TimeInput;
