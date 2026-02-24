/**
 * Appointment-related TypeScript types
 * Matching the backend DTOs
 */

export type AppointmentStatus = 'SCHEDULED' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';

export interface AppointmentResponse {
	id: number;
	scheduledDateTime: string;
	reason: string;
	status: AppointmentStatus;
	petId: number;
	petName: string;
	vetId: number;
	vetFirstName: string;
	vetLastName: string;
}

export interface CreateAppointmentRequest {
	scheduledDateTime: string;
	reason: string;
	status?: AppointmentStatus;
	petId: number;
	vetId: number;
}

export interface UpdateAppointmentRequest {
	scheduledDateTime: string;
	reason: string;
	status: AppointmentStatus;
}
