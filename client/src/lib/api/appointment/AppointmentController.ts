/**
 * Appointment API Controller
 *
 * Thin API layer for appointment-related operations.
 * Handles HTTP communication only - no business logic.
 */
import { client } from '$lib/api/client';
import type {
	AppointmentResponse,
	CreateAppointmentRequest,
	UpdateAppointmentRequest
} from '$lib/api/appointment/types';

/**
 * Get all appointments
 */
export async function getAppointments(): Promise<AppointmentResponse[]> {
	const { data, error } = await client.GET('/v1/appointments');
	if (error) throw error;
	return data ?? [];
}

/**
 * Get appointment by ID
 */
export async function getAppointmentById(appointmentId: number): Promise<AppointmentResponse> {
	const { data, error } = await client.GET('/v1/appointments/{appointmentId}', {
		params: { path: { appointmentId } }
	});
	if (error) throw error;
	if (!data) throw new Error('Appointment not found');
	return data;
}

/**
 * Create a new appointment
 */
export async function createAppointment(request: CreateAppointmentRequest): Promise<AppointmentResponse> {
	const { data, error } = await client.POST('/v1/appointments', {
		body: request
	});
	if (error) throw error;
	if (!data) throw new Error('Failed to create appointment');
	return data;
}

/**
 * Update an existing appointment
 */
export async function updateAppointment(
	appointmentId: number,
	request: UpdateAppointmentRequest
): Promise<AppointmentResponse> {
	const { data, error } = await client.PUT('/v1/appointments/{appointmentId}', {
		params: { path: { appointmentId } },
		body: request
	});
	if (error) throw error;
	if (!data) throw new Error('Failed to update appointment');
	return data;
}

/**
 * Cancel an appointment
 */
export async function cancelAppointment(appointmentId: number): Promise<AppointmentResponse> {
	const { data, error } = await client.PATCH('/v1/appointments/{appointmentId}/cancel', {
		params: { path: { appointmentId } }
	});
	if (error) throw error;
	if (!data) throw new Error('Failed to cancel appointment');
	return data;
}

/**
 * Complete an appointment
 */
export async function completeAppointment(appointmentId: number): Promise<AppointmentResponse> {
	const { data, error } = await client.PATCH('/v1/appointments/{appointmentId}/complete', {
		params: { path: { appointmentId } }
	});
	if (error) throw error;
	if (!data) throw new Error('Failed to complete appointment');
	return data;
}

/**
 * Delete an appointment
 */
export async function deleteAppointment(appointmentId: number): Promise<void> {
	const { error } = await client.DELETE('/v1/appointments/{appointmentId}', {
		params: { path: { appointmentId } }
	});
	if (error) throw error;
}
